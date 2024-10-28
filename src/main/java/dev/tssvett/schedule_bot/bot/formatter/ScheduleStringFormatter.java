package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ScheduleStringFormatter {
    private static final List<String> DAYS_OF_WEEK = List.of("понедельник", "вторник", "среда", "четверг",
            "пятница", "суббота", "воскресенье");

    public String formatWeek(Map<String, List<LessonInfoDto>> weekLessons) {
        StringBuilder weekScheduleStringBuilder = new StringBuilder();

        DAYS_OF_WEEK.stream()
                .filter(day -> dayHasLessons(weekLessons.get(day)))
                .forEach(day -> weekScheduleStringBuilder.append(existingEducationalDayToString(day, weekLessons.get(day))));

        return weekScheduleStringBuilder.toString();
    }

    public String formatDay(Map<String, List<LessonInfoDto>> lessonsByDay, String weekDayName) {

        StringBuilder dayScheduleStringBuilder = new StringBuilder();
        List<LessonInfoDto> dayLessons = lessonsByDay.get(weekDayName);

        if (dayHasLessons(dayLessons)) {
            dayScheduleStringBuilder.append(existingEducationalDayToString(weekDayName, dayLessons));
        } else {
            dayScheduleStringBuilder.append(emptyEducationalDayToString(weekDayName));
        }

        return dayScheduleStringBuilder.toString();
    }

    private String existingEducationalDayToString(String weekDayName, @NotNull List<LessonInfoDto> dayLessons) {
        StringBuilder sb = new StringBuilder();
        String lessonDate = dayLessons.get(0).dateNumber();

        sb.append(dayHeaderToString(weekDayName, lessonDate));
        dayLessons.forEach(lesson -> sb.append(lessonToString(lesson)));
        sb.append("\n");
        return sb.toString();
    }

    private String emptyEducationalDayToString(String weekDayName) {
        StringBuilder sb = new StringBuilder();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        sb.append(String.format("""
                %s %s (%s):
                Зачилься, пар нет :)
                """, MessageTextConstantsUtils.DAY_HEADER, capitalizeFirstLetter(weekDayName), currentDate));
        sb.append("\n");

        return sb.toString();
    }

    private String dayHeaderToString(String day, String lessonDate) {
        return String.format("""
                %s %s (%s):
                """, MessageTextConstantsUtils.DAY_HEADER, capitalizeFirstLetter(day), lessonDate);
    }

    private String lessonToString(LessonInfoDto lesson) {
        return String.format("""
                        %s %s
                        Тип: %s
                        Место: %s
                        Время: %s
                                    
                        """, getEmojiForLesson(lesson), capitalizeFirstLetter(lesson.name()),
                lesson.type(),
                lesson.place(),
                lesson.time());
    }

    private String getEmojiForLesson(LessonInfoDto lesson) {
        String place = lesson.place().toLowerCase();
        String type = lesson.type().toLowerCase();
        String name = lesson.name().toLowerCase();

        if ("online".equals(place)) {
            return MessageTextConstantsUtils.ONLINE_EMOJI;
        }

        return switch (type) {
            case "лабораторная" -> MessageTextConstantsUtils.LAB_EMOJI;
            case "лекция" -> MessageTextConstantsUtils.LECTURE_EMOJI;
            default -> "военная кафедра".equals(name) ? MessageTextConstantsUtils.MILITARY_EMOJI
                    : MessageTextConstantsUtils.DEFAULT_EMOJI;
        };
    }

    private boolean dayHasLessons(List<LessonInfoDto> dayLessons) {
        return dayLessons != null && !dayLessons.isEmpty();
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
