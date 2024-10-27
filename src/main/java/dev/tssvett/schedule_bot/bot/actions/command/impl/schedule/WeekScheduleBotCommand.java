package dev.tssvett.schedule_bot.bot.actions.command.impl.schedule;


import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.service.ScheduleService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeekScheduleBotCommand implements BotCommand {
    private final ScheduleService scheduleService;
    private final ScheduleStringFormatter scheduleStringFormatter;

    @Override
    @RegistrationRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        Map<String, List<LessonInfoDto>> weekSchedule = scheduleService.getWeekScheduleMapByDate(userId);
        String formattedLessons = scheduleStringFormatter.formatWeek(weekSchedule);

        return SendMessage.builder()
                .chatId(chatId)
                .text(formattedLessons)
                .build();
    }
}

