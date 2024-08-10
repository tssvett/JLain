package dev.tssvett.schedule_bot.schedule.parser;

import dev.tssvett.schedule_bot.schedule.group.Group;
import dev.tssvett.schedule_bot.exceptions.ConnectionException;
import dev.tssvett.schedule_bot.exceptions.ParseException;
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
public class GroupParser implements Parser<Group> {

    private static final String URL = String.format("https://ssau.ru/rasp/faculty/%d?course=%d",492430598, 3);
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GROUP_SELECTOR = "body > div.container.timetable > div > div > div > a";

    @Override
    public List<Group> parse(){
        Document document = null;
        try {
            document = Jsoup.connect(URL).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
        Elements rawFaculties = document.select(GROUP_SELECTOR);
        return parseAll(rawFaculties);
    }

    private List<Group> parseAll(Elements rawGroups) {
        List<Group> groups = new ArrayList<>();
        for (org.jsoup.nodes.Element rawGroup : rawGroups) {
            String rawHref = rawGroup.attr("href");
            String rawName = rawGroup.select("a.btn-text.group-catalog__group span").first().text();
            Group group = Group.builder()
                    .id(parseGroupId(rawHref))
                    .name(rawName)
                    .build();
            groups.add(group);
        }
        return groups;
    }

    private String parseGroupId(String href) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new ParseException("Ошибка при айди группы");
        }
    }
}
