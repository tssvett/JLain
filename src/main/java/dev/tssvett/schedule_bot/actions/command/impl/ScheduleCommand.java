package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.entity.Group;
import dev.tssvett.schedule_bot.repository.GroupRepository;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.Lesson;
import dev.tssvett.schedule_bot.schedule.parser.SchoolWeekParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleCommand implements Command {
    private final SchoolWeekParser schoolWeekParser;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);

        String groupName = userRepository.findById(userId).get().getGroupName();
        List<Group> groups = groupRepository.findAll();
        Long groupId = 0L;
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                groupId = group.getGroupId();
                break;
            }
        }
        List<Lesson> lessons = schoolWeekParser.parse(groupId, 1);
        List<String> stringLessons = lessons.stream().map(this::vremenniyToString).toList();
        String end = "";
        for (int i = 0; i < stringLessons.size(); i++) {
            end += stringLessons.get(i) + "\n";
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(end)
                //.text(MessageConstants.SCHEDULE_COMMAND)
                .build();
    }

    private String vremenniyToString(Lesson lesson) {
        return lesson.getDateDay() + " " + lesson.getTime() + " " + lesson.getName();
    }
}
