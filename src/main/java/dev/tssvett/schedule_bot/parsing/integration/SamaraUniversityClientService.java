package dev.tssvett.schedule_bot.parsing.integration;

import dev.tssvett.schedule_bot.backend.exception.parse.ParserSourceConnectionException;
import dev.tssvett.schedule_bot.bot.properties.SamaraUniversityProperties;
import dev.tssvett.schedule_bot.parsing.enums.Connection;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SamaraUniversityClientService {
    private final SamaraUniversityProperties properties;

    public Document getFacultiesHtml() {
        try {
            String url = properties.facultyUrl();

            return Jsoup.connect(url)
                    .userAgent(Connection.USER_AGENT.getName())
                    .get();
        } catch (IOException e) {
            throw new ParserSourceConnectionException(e.getMessage());
        }
    }

    public Document getGroupsHtml(long facultyId, long course) {
        try {
            String url = String.format(properties.groupUrl(), facultyId, course);

            return Jsoup.connect(url)
                    .userAgent(Connection.USER_AGENT.getName())
                    .get();
        } catch (IOException e) {
            throw new ParserSourceConnectionException(e.getMessage());
        }
    }

    public Document getLessonsHtml(Long groupId, Integer week) {
        try {
            String url = String.format(properties.lessonUrl(), groupId, week);

            return Jsoup.connect(url)
                    .userAgent(Connection.USER_AGENT.getName())
                    .get();
        } catch (IOException e) {
            throw new ParserSourceConnectionException(e.getMessage());
        }
    }
}
