package dev.tssvett.schedule_bot.bot.actions.command.impl.schedule;


import dev.tssvett.schedule_bot.bot.actions.command.Command;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.backend.entity.Lesson;
import dev.tssvett.schedule_bot.parsing.parser.SchoolWeekParser;
import dev.tssvett.schedule_bot.bot.utils.CurrentDateCalculator;
import dev.tssvett.schedule_bot.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeekScheduleCommand implements Command {
    private final SchoolWeekParser schoolWeekParser;
    private final UserService userService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final CurrentDateCalculator currentDateCalculator;

    @Override
    @RegistrationRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        String groupName = userService.findUserById(userId).getGroup().getName();
        Long groupId = userService.getUserGroupIdByGroupName(groupName);
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(groupId, currentDateCalculator.calculateWeekNumber());
        String formattedLessons = scheduleStringFormatter.formatWeek(lessonsInWeek);
        return SendMessage.builder()
                .chatId(chatId)
                .text(formattedLessons)
                .build();
    }
}

