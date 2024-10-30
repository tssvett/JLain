package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.bot.enums.DaysOfWeek;
import dev.tssvett.schedule_bot.bot.enums.LessonType;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ScheduleStringFormatter {

    public String formatWeek(Map<String, List<LessonInfoDto>> weekLessons) {
        StringBuilder weekScheduleStringBuilder = new StringBuilder();

        Arrays.stream(DaysOfWeek.values())
                .filter(day -> dayHasLessons(weekLessons.get(day.getName())))
                .forEach(day -> weekScheduleStringBuilder.append(existingEducationalDayToString(day.getName(),
                        weekLessons.get(day.getName()))));

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

    private String existingEducationalDayToString(String weekDayName, List<LessonInfoDto> dayLessons) {
        StringBuilder sb = new StringBuilder();
        String lessonDate = dayLessons.get(0).dateNumber();
        sb.append(dayHeaderToString(weekDayName, lessonDate));
        sb.append("\n");

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
                %s %s (%s)
                """, MessageTextConstantsUtils.DAY_HEADER, capitalizeFirstLetter(day), lessonDate);
    }

    private String lessonToString(LessonInfoDto lesson) {
        return String.format("""
                        %s %s | %s | %s%s
                                    
                        """, getEmojiForLesson(lesson), lesson.time(), capitalizeFirstLetter(lesson.name()),
                lesson.place(),
                lesson.subgroup().equals(Subgroup.EMPTY) ? "" : "\nПодгруппа: " + lesson.subgroup().getName());
    }

    private String getEmojiForLesson(LessonInfoDto lesson) {
        return switch (lesson.type()) {
            case LABORATORY -> MessageTextConstantsUtils.LAB_EMOJI;
            case LECTURE -> MessageTextConstantsUtils.LECTURE_EMOJI;
            case PRACTICE -> MessageTextConstantsUtils.PRACTICE_EMOJI;
            case ANOTHER -> MessageTextConstantsUtils.OTHER_EMOJI;
            case EXAM -> MessageTextConstantsUtils.EXAM_EMOJI;
            default -> "";
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
