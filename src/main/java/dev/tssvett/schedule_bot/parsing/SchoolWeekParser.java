package dev.tssvett.schedule_bot.parsing;

import dev.tssvett.schedule_bot.backend.exception.parse.ParserSourceConnectionException;
import dev.tssvett.schedule_bot.bot.enums.LessonType;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;
import dev.tssvett.schedule_bot.persistence.entity.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
public class SchoolWeekParser {
    private static final String URL = "https://ssau.ru/rasp?groupId=%d&selectedWeek=%d";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SCHOOL_WEEK_SELECTOR = ".schedule__item";
    private static final String TIME_SELECTOR = ".schedule__time";

    public List<Lesson> parse(Long groupId, Integer week) {
        Document document = fetchDocument(groupId, week);
        Elements rawSchoolWeek = document.select(SCHOOL_WEEK_SELECTOR);
        List<String> lessonTimes = parseLessonTimes(document);
        List<String> lessonDates = parseLessonDates(document);
        List<Lesson> lessons = parseAll(rawSchoolWeek, lessonTimes, lessonDates);

        return sortLessonsByDay(lessons, lessonDates);
    }

    private Document fetchDocument(Long groupId, Integer week) {
        try {
            return Jsoup.connect(String.format(URL, groupId, week)).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            throw new ParserSourceConnectionException(e.getMessage());
        }
    }

    private List<String> parseLessonTimes(Document document) {
        return document.select(TIME_SELECTOR).stream()
                .map(timeElement -> {
                    String[] timeParts = timeElement.text().split(" ");
                    return (timeParts.length == 2) ? timeParts[0] + " - " + timeParts[1] : "";
                })
                .filter(time -> !time.isEmpty())
                .toList();
    }

    private List<String> parseLessonDates(Document document) {
        Elements rawDates = document.select(SCHOOL_WEEK_SELECTOR);
        return IntStream.range(1, Math.min(7, rawDates.size())) // Пропускаем первый элемент
                .mapToObj(rawDates::get)
                .map(Element::text)
                .toList();
    }

    private List<Lesson> parseAll(Elements schoolWeek, List<String> lessonTimes, List<String> dates) {
        List<Lesson> lessonsList = new LinkedList<>();
        Elements potentialLessons = removeFirstNElements(schoolWeek, 7);
        for (int i = 0; i < potentialLessons.size(); i++) {
            Elements lessons = potentialLessons.get(i).select(".schedule__lesson");
            for (Element lessonElement : lessons) {
                if (!lessons.isEmpty()) {
                    lessonsList.add(buildLesson(lessonTimes, dates, lessonElement, i));
                }
            }
        }
        return lessonsList;
    }

    private Lesson buildLesson(List<String> lessonTimes, List<String> dates, Element lessonElement, int i) {
        return Lesson.builder()
                .name(getName(lessonElement))
                .type(getType(lessonElement))
                .place(getPlace(lessonElement))
                .teacher(getTeacher(lessonElement))
                .subgroup(getSubgroupString(lessonElement))
                .time(getTime(lessonTimes, dates, i))
                .dateDay(getDateDay(dates, i))
                .dateNumber(getDateNumber(dates, i))
                .build();
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
        String subgroupText = extractSubgroupText(lessonElement);
        return getSubgroupFromText(subgroupText);
    }

    private static String extractSubgroupText(Element lessonElement) {
        String subgroup = lessonElement.select(".schedule__groups").text().toLowerCase();

        // Use regex to find the subgroup after "подгруппы:"
        String regex = "подгруппы:\\s*(\\S+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(subgroup);

        if (matcher.find()) {
            return matcher.group(1); // Return the first captured group
        }

        return ""; // Return an empty string if no subgroup found
    }

    private static Subgroup getSubgroupFromText(String subgroupText) {
        try {
            return Subgroup.fromName(subgroupText);
        } catch (IllegalArgumentException e) {
            return Subgroup.EMPTY;
        }
    }

    private static String getTeacher(Element lessonElement) {
        return lessonElement.select(".schedule__teacher").text().toLowerCase();
    }

    private static String getPlace(Element lessonElement) {
        return lessonElement.select(".schedule__place").text().toLowerCase();
    }

    private static LessonType getType(Element lessonElement) {
        String type = lessonElement.select(".schedule__lesson-type").text().toLowerCase();
        try {
            return LessonType.fromName(type);
        } catch (IllegalArgumentException e) {
            return LessonType.fromName(type.split(" ")[0]);
        }
    }

    private static String getName(Element lessonElement) {
        return lessonElement.select(".schedule__discipline").text().toLowerCase();
    }

    private static Elements removeFirstNElements(Elements elements, int n) {
        return new Elements(IntStream.range(n, elements.size())
                .mapToObj(elements::get)
                .toList());
    }

    private List<Lesson> sortLessonsByDay(List<Lesson> lessons, List<String> dates) {
        return dates.stream().map(date -> {
                    String dayOfWeek = date.split(" ")[0];
                    return lessons.stream()
                            .filter(lesson -> lesson.getDateDay().equals(dayOfWeek))
                            .toList();
                })
                .flatMap(List::stream)
                .toList();
    }

}
