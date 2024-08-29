package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.schedule.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.schedule.lesson.Lesson;
import dev.tssvett.schedule_bot.schedule.parser.SchoolWeekParser;
import dev.tssvett.schedule_bot.schedule.utils.CurrentDateCalculator;
import dev.tssvett.schedule_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleCommand implements Command {
    private final SchoolWeekParser schoolWeekParser;
    private final UserService userService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final CurrentDateCalculator currentDateCalculator;

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        String groupName = userService.findUserById(userId).getGroupName();
        Long groupId = userService.getUserGroupIdByGroupName(groupName);
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(groupId, currentDateCalculator.calculateWeekNumber());
        String formattedLessons = scheduleStringFormatter.formatWeek(lessonsInWeek);
        return SendMessage.builder()
                .chatId(chatId)
                .text(formattedLessons)
                .build();
    }
}
