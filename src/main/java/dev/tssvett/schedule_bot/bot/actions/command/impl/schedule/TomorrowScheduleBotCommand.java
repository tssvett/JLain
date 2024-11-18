package dev.tssvett.schedule_bot.bot.actions.command.impl.schedule;


import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.service.LessonService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
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
public class TomorrowScheduleBotCommand implements BotCommand {
    private final LessonService lessonService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final DateUtils dateUtils;

    @Override
    @RegistrationRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        Map<String, List<LessonInfoDto>> weekSchedule = lessonService.getWeekScheduleMapByDate(userId);
        String stringLessons = scheduleStringFormatter.formatDay(weekSchedule, dateUtils.calculateTomorrowDayName());

        return SendMessage.builder()
                .chatId(chatId)
                .text(stringLessons)
                .build();
    }
}

