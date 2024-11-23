package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.bot.enums.DaysOfWeek;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleStringFormatter {
    private final DateUtils dateUtils;

    public String formatWeek(Map<String, List<LessonInfoDto>> weekLessons) {
        StringBuilder weekScheduleStringBuilder = new StringBuilder();

        Arrays.stream(DaysOfWeek.values())
                .filter(day -> dayHasLessons(weekLessons.get(day.getName())))
                .forEach(day -> weekScheduleStringBuilder.append(existingEducationalDayToString(day.getName(),
                        weekLessons.get(day.getName()))));

        return weekScheduleStringBuilder.toString();
    }

    public String formatDay(Map<String, List<LessonInfoDto>> lessonsByDay, String weekDayName) {
        StringBuilder dayStringBuilder = new StringBuilder();

        List<LessonInfoDto> dayLessons = lessonsByDay.get(weekDayName);
        if (dayHasLessons(dayLessons)) {
            dayStringBuilder.append(existingEducationalDayToString(weekDayName, dayLessons));
        } else {
            dayStringBuilder.append(emptyEducationalDayToString(weekDayName));
        }

        return dayStringBuilder.toString();
    }

    public String formatNotificationMessage(Map<String, List<LessonInfoDto>> lessonsInWeek) {
        String tomorrowDayName = dateUtils.calculateTomorrowDayName();
        String formattedDay = this.formatDay(lessonsInWeek, tomorrowDayName);

        return String.format("""
                Уведомление! Расписание на завтра
                            
                %s
                """, formattedDay);
    }

    private String existingEducationalDayToString(String weekDayName, List<LessonInfoDto> dayLessons) {
        StringBuilder dayStringBuilder = new StringBuilder();

        dayStringBuilder.append(MessageCreateUtils.createDayHeader(weekDayName, dayLessons.get(0).dateNumber()));
        dayLessons.forEach(lesson -> dayStringBuilder.append(MessageCreateUtils.createStringLesson(lesson)));
        dayStringBuilder.append("\n");

        return dayStringBuilder.toString();
    }

    private String emptyEducationalDayToString(String weekDayName) {
        return MessageCreateUtils.createNotExistingDayMessage(weekDayName, dateUtils.getCurrentDate());
    }

    private boolean dayHasLessons(List<LessonInfoDto> dayLessons) {
        return dayLessons != null && !dayLessons.isEmpty();
    }
}
