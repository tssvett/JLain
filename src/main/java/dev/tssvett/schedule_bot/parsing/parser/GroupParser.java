package dev.tssvett.schedule_bot.parsing.parser;

import dev.tssvett.schedule_bot.backend.exception.parse.ParseElementException;
import dev.tssvett.schedule_bot.parsing.dto.GroupParserDto;
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
public class GroupParser {
    private final SamaraUniversityClientService samaraUniversityClientService;

    public List<GroupParserDto> parse(Long facultyId, Integer course) {
        List<GroupParserDto> educationalGroups = new ArrayList<>();

        samaraUniversityClientService.getGroupsHtml(facultyId, course)
                .ifPresentOrElse(
                groupsHtml -> groupsHtml.select(Selector.GROUP_PAGE_SELECTOR.getName())
                        .forEach(element -> {
                            long groupId = parseGroupId(element.attr(Selector.GROUP_ID_SELECTOR.getName()));
                            String groupName = element.select(Selector.GROUP_NAME_SELECTOR.getName()).first().text();
                            educationalGroups.add(new GroupParserDto(groupId, groupName));
                        }),

                () -> log.error("Ошибка при парсинге групп")
        );

        return educationalGroups;
    }


    private Long parseGroupId(String href) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(href);
        if (!matcher.find()) {
            throw new ParseElementException("Ошибка при айди группы");
        }

        return Long.parseLong(matcher.group());
    }
}
