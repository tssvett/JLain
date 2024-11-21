package dev.tssvett.schedule_bot.parsing.parser;

import dev.tssvett.schedule_bot.bot.enums.LessonType;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;
import dev.tssvett.schedule_bot.parsing.dto.LessonParserDto;
import dev.tssvett.schedule_bot.parsing.enums.Selector;
import dev.tssvett.schedule_bot.parsing.integration.SamaraUniversityClientService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LessonParser {
    private final SamaraUniversityClientService samaraUniversityClientService;
    private static final int TIME_ELEMENTS_NUMBER = 7;
    private static final int UNIFIED_TIME_PARTS_NUMBER = 2;

    public List<LessonParserDto> parse(Long groupId, Integer week) {
        Optional<Document> optionalLessonDocument = samaraUniversityClientService.getLessonsHtml(groupId, week);

        if (optionalLessonDocument.isEmpty()) {
            return new ArrayList<>();
        }

        Document lessonsHtmlDocument = optionalLessonDocument.get();

        List<String> lessonTimes = getLessonTimesFromHtml(lessonsHtmlDocument);
        List<String> lessonDates = getLessonDatesFromHtml(lessonsHtmlDocument);

        if (lessonTimes.isEmpty() || lessonDates.isEmpty()) {
            return new ArrayList<>();
        }

        List<LessonParserDto> lessons = createLessonRecords(lessonsHtmlDocument, lessonTimes, lessonDates);

        return sortLessonsByDay(lessons, lessonDates);
    }

    private List<String> getLessonTimesFromHtml(Document elements) {
        return elements.select(Selector.TIME_SELECTOR.getName())
                .stream()
                .map(timeElement -> {
                    String[] timeParts = timeElement.text().split(" ");
                    return timeParts.length == UNIFIED_TIME_PARTS_NUMBER
                            ? extractUnifiedTime(timeParts)
                            : extractNotUnifiedTime(timeParts);
                })
                .filter(time -> !time.isEmpty())
                .toList();
    }

    /**
     * Для формата времени [8.00, 9,45]. Формат справедливен для всей недели(единый)
     *
     * @param timeParts Массив строк
     * @return Время урока по дням недели, в формате 8.00 - 9.45
     */
    private static String extractUnifiedTime(String[] timeParts) {
        return timeParts[0] + " - " + timeParts[1];
    }

    /**
     * @param timeParts Массив строк
     * @return Время уроков по дням недели, в формате Пн-Пт 8.00 - 9.45, Сб 8.00 - 9.45
     */
    private static String extractNotUnifiedTime(String[] timeParts) {
        return String.format("%s %s - %s, %s %s - %s", timeParts[0], timeParts[1], timeParts[2], timeParts[3],
                timeParts[4], timeParts[5]);
    }

    private List<String> getLessonDatesFromHtml(Document document) {
        Elements rawDates = document.select(Selector.SCHOOL_WEEK_SELECTOR.getName());

        return IntStream.range(1, Math.min(TIME_ELEMENTS_NUMBER, rawDates.size())) // Пропускаем первый элемент
                .mapToObj(rawDates::get)
                .map(Element::text)
                .toList();
    }

    private List<LessonParserDto> createLessonRecords(Document schoolWeek, List<String> lessonTimes,
                                                      List<String> dates) {
        List<LessonParserDto> lessonsList = new LinkedList<>();

        Elements lessonsElements = removeFirstNElements(
                schoolWeek.select(Selector.SCHOOL_WEEK_SELECTOR.getName()), TIME_ELEMENTS_NUMBER
        );

        for (int i = 0; i < lessonsElements.size(); i++) {
            Elements lessons = lessonsElements.get(i).select(Selector.LESSON_SELECTOR.getName());
            for (Element lessonElement : lessons) {
                lessonsList.add(buildLesson(lessonTimes, dates, lessonElement, i));
            }
        }

        return lessonsList;
    }

    private LessonParserDto buildLesson(List<String> lessonTimes, List<String> dates, Element lessonElement, int i) {
        return new dev.tssvett.schedule_bot.parsing.dto.LessonParserDto(
                UUID.randomUUID(),
                getName(lessonElement),
                getType(lessonElement).getName(),
                getPlace(lessonElement),
                getTeacher(lessonElement),
                getSubgroupString(lessonElement).getName(),
                getTime(lessonTimes, dates, i),
                getDateDay(dates, i),
                getDateNumber(dates, i)
        );
    }

    private static String getDateNumber(List<String> dates, int i) {
        return dates.get(i % dates.size()).split(" ")[1];
    }

    private static String getDateDay(List<String> dates, int i) {
        return dates.get(i % dates.size()).split(" ")[0];
    }

    private static String getTime(List<String> lessonTimes, List<String> dates, int i) {
        return lessonTimes.get(i / dates.size());
    }

    private static Subgroup getSubgroupString(Element lessonElement) {
        return getSubgroupFromText(extractSubgroupText(lessonElement));
    }

    private static String extractSubgroupText(Element lessonElement) {
        String subgroup = lessonElement.select(Selector.GROUPS_SELECTOR.getName()).text().toLowerCase();

        String regex = "подгруппы:\\s*(\\S+)";
        Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(subgroup);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private static Subgroup getSubgroupFromText(String subgroupText) {
        try {
            return Subgroup.fromName(subgroupText);
        } catch (IllegalArgumentException e) {
            return Subgroup.EMPTY;
        }
    }

    private static String getTeacher(Element lessonElement) {
        return lessonElement.select(Selector.TEACHER_SELECTOR.getName()).text().toLowerCase();
    }

    private static String getPlace(Element lessonElement) {
        return lessonElement.select(Selector.PLACE_SELECTOR.getName()).text().toLowerCase();
    }

    private static LessonType getType(Element lessonElement) {
        String type = lessonElement.select(Selector.LESSON_TYPE_SELECTOR.getName()).text().toLowerCase();
        try {
            return LessonType.fromName(type);
        } catch (IllegalArgumentException e) {
            return LessonType.fromName(type.split(" ")[0]);
        }
    }

    private static String getName(Element lessonElement) {
        return lessonElement.select(Selector.DISCIPLINE_SELECTOR.getName()).text().toLowerCase();
    }

    @SuppressWarnings("SameParameterValue")
    private static Elements removeFirstNElements(Elements elements, int n) {
        return new Elements(IntStream.range(n, elements.size())
                .mapToObj(elements::get)
                .toList());
    }

    private List<LessonParserDto> sortLessonsByDay(List<LessonParserDto> lessons, List<String> dates) {
        return dates.stream()
                .map(date -> {
                    String dayOfWeek = date.split(" ")[0];
                    return lessons.stream()
                            .filter(lesson -> lesson.dateDay().equals(dayOfWeek))
                            .toList();
                })
                .flatMap(List::stream)
                .toList();
    }
}
