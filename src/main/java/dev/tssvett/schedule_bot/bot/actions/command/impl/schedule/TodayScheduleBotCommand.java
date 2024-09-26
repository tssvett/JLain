package dev.tssvett.schedule_bot.bot.actions.command.impl.schedule;

import dev.tssvett.schedule_bot.backend.entity.Student;
import dev.tssvett.schedule_bot.backend.entity.Lesson;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.bot.utils.CurrentDateCalculator;
import dev.tssvett.schedule_bot.parsing.SchoolWeekParser;
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
    private final SchoolWeekParser schoolWeekParser;
    private final StudentService studentService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final CurrentDateCalculator currentDateCalculator;

    @Override
    @RegistrationRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        Student student = studentService.findStudentById(userId);
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(student.getGroup().getGroupId(), currentDateCalculator.calculateWeekNumber());
        String formattedLessons = scheduleStringFormatter.formatDay(lessonsInWeek, currentDateCalculator.calculateCurrentDayName());

        return SendMessage.builder()
                .chatId(chatId)
                .text(formattedLessons)
                .build();
    }
}
