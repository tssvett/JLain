package dev.tssvett.schedule_bot.schedule.parser;

import dev.tssvett.schedule_bot.exception.ConnectionException;
import dev.tssvett.schedule_bot.exception.ParseException;
import dev.tssvett.schedule_bot.entity.Faculty;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component

public class FacultyParser implements Parser<Faculty> {

    private static final String URL = "https://ssau.ru/rasp";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String FACULTIES_SELECTOR = "body > div.container.timetable > div.timetable__content > div" +
            " > div > div > a";

    @Override
    public List<Faculty> parse() {
        Document document = null;
        try {
            document = Jsoup.connect(URL).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
        Elements rawFaculties = document.select(FACULTIES_SELECTOR);
        return parseAll(rawFaculties);
    }

    private List<Faculty> parseAll(Elements rawFaculties) {
        List<Faculty> faculties = new ArrayList<>();
        for (org.jsoup.nodes.Element rawFaculty : rawFaculties) {
            String rawHref = rawFaculty.attr("href");
            String rawName = rawFaculty.select("a.h3-text").get(0).text();
            Faculty faculty = Faculty.builder()
                    .facultyId(Long.parseLong(parseFacultyId(rawHref)))
                    .name(rawName)
                    .build();
            faculties.add(faculty);
        }

        return faculties;
    }

    private String parseFacultyId(String href) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new ParseException("Ошибка при парсинге айди факультета");
        }
    }
}
