package dev.tssvett.schedule_bot.bot.actions.command.impl.schedule;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.service.ScheduleService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.bot.utils.CurrentDateCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayScheduleBotCommand implements BotCommand {
    private final ScheduleService scheduleService;
    private final CurrentDateCalculator currentDateCalculator;
    private final ScheduleStringFormatter scheduleStringFormatter;

    @Override
    @RegistrationRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        List<LessonInfoDto> lessonsInWeek = scheduleService.getWeekSchedule(userId);
        String stringLessons = scheduleStringFormatter.formatDay(lessonsInWeek,
                currentDateCalculator.calculateCurrentDayName());

        return SendMessage.builder()
                .chatId(chatId)
                .text(stringLessons)
                .build();
    }
}
