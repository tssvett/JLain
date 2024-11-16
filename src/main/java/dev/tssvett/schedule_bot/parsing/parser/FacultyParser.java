package dev.tssvett.schedule_bot.parsing.parser;

import dev.tssvett.schedule_bot.backend.exception.parse.ParseElementException;
import dev.tssvett.schedule_bot.parsing.dto.FacultyParserDto;
import dev.tssvett.schedule_bot.parsing.enums.Selector;
import dev.tssvett.schedule_bot.parsing.integration.SamaraUniversityClientService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyParser {
    private final SamaraUniversityClientService samaraUniversityClientService;

    public List<FacultyParserDto> parse() {
        List<FacultyParserDto> faculties = new ArrayList<>();

        samaraUniversityClientService.getFacultiesHtml().ifPresentOrElse(
                document -> document.select(Selector.FACULTY_PAGE_SELECTOR.getName())
                        .forEach((element -> {
                            long facultyId = parseFacultyId(element.attr(Selector.FACULTY_ID_SELECTOR.getName()));
                            String facultyName = element.select(Selector.FACULTY_NAME_SELECTOR.getName()).get(0).text();
                            faculties.add(new FacultyParserDto(facultyId, facultyName));
                        })),

                () -> log.error("Факультеты не найдены")
        );

        return faculties;
    }

    private Long parseFacultyId(String href) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(href);
        if (!matcher.find()) {
            throw new ParseElementException("Ошибка при парсинге айди факультета");
        }

        return Long.parseLong(matcher.group());
    }
}
