package dev.tssvett.schedule_bot.parsing.parser;

import dev.tssvett.schedule_bot.backend.exception.ConnectionException;
import dev.tssvett.schedule_bot.backend.entity.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
            throw new ConnectionException(e);
        }
    }

    private List<String> parseLessonTimes(Document document) {
        return document.select(TIME_SELECTOR).stream()
                .map(timeElement -> {
                    String[] timeParts = timeElement.text().split(" ");
                    return (timeParts.length == 2) ? timeParts[0] + " - " + timeParts[1] : "";
                })
                .filter(time -> !time.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> parseLessonDates(Document document) {
        Elements rawDates = document.select(SCHOOL_WEEK_SELECTOR);
        return IntStream.range(1, Math.min(7, rawDates.size())) // Пропускаем первый элемент
                .mapToObj(rawDates::get)
                .map(Element::text)
                .collect(Collectors.toList());
    }

    private List<Lesson> parseAll(Elements schoolWeek, List<String> lessonTimes, List<String> dates) {
        List<Lesson> lessons = new LinkedList<>();
        Elements remainingElements = removeFirstNElements(schoolWeek, 7);
        for (int i = 0; i < remainingElements.size(); i++) {
            Lesson lesson = Lesson.builder()
                    .name(remainingElements.get(i).select(".schedule__discipline").text())
                    .type(remainingElements.get(i).select(".schedule__lesson-type").text())
                    .place(remainingElements.get(i).select(".schedule__place").text())
                    .teacher(remainingElements.get(i).select(".schedule__teacher").text())
                    .subgroup(remainingElements.get(i).select(".schedule__groups").text())
                    .time(lessonTimes.get(i / dates.size()))
                    .dateDay(dates.get(i % dates.size()).split(" ")[0]) // Используем даты
                    .dateNumber(dates.get(i % dates.size()).split(" ")[1])
                    .build();
            lessons.add(lesson);
        }
        return lessons;
    }

    private static Elements removeFirstNElements(Elements elements, int n) {
        return new Elements(IntStream.range(n, elements.size())
                .mapToObj(elements::get)
                .collect(Collectors.toList()));
    }

    private List<Lesson> sortLessonsByDay(List<Lesson> lessons, List<String> dates) {
        return dates.stream().map(date -> {
                    String dayOfWeek = date.split(" ")[0];
                    return lessons.stream()
                            .filter(lesson -> lesson.getDateDay().equals(dayOfWeek))
                            .collect(Collectors.toList());
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
