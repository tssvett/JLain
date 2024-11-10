package dev.tssvett.schedule_bot.parsing;

import dev.tssvett.schedule_bot.backend.exception.parse.ParseElementException;
import dev.tssvett.schedule_bot.backend.exception.parse.ParserSourceConnectionException;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component

public class FacultyParser implements Parser<FacultyRecord> {

    private static final String URL = "https://ssau.ru/rasp";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String FACULTIES_SELECTOR = "body > div.container.timetable > div.timetable__content > div" +
            " > div > div > a";

    @Override
    public List<FacultyRecord> parse() {
        Document document = null;
        try {
            document = Jsoup.connect(URL).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            throw new ParserSourceConnectionException(e.getMessage());
        }
        Elements rawFaculties = document.select(FACULTIES_SELECTOR);

        return parseAll(rawFaculties);
    }

    private List<FacultyRecord> parseAll(Elements rawFaculties) {
        List<FacultyRecord> faculties = new ArrayList<>();
        for (Element rawFaculty : rawFaculties) {
            String rawHref = rawFaculty.attr("href");
            String rawName = rawFaculty.select("a.h3-text").get(0).text();
            faculties.add(new FacultyRecord(Long.parseLong(parseFacultyId(rawHref)), rawName));
        }

        return faculties;
    }

    private String parseFacultyId(String href) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new ParseElementException("Ошибка при парсинге айди факультета");
        }
    }
}
