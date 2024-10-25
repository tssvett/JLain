package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.persistence.entity.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScheduleStringFormatter {

    private static final String ONLINE_EMOJI = "🟢"; // Зеленый круг для online
    private static final String LAB_EMOJI = "🟣"; // Фиолетовый круг для лабораторной
    private static final String LECTURE_EMOJI = "🟡"; // Желтый круг для лекций
    private static final String MILITARY_EMOJI = "🟠"; // Оранжевый круг для военных занятий
    private static final String DEFAULT_EMOJI = "🔴"; // Красный круг для очных занятий
    private static final String DAY_HEADER = "🔹";

    private static final List<String> DAYS_OF_WEEK = List.of("понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье");

    public String formatWeek(List<LessonInfoDto> weekLessons) {
        Map<String, List<LessonInfoDto>> lessonsByDays = groupLessonsByDay(weekLessons);
        StringBuilder sb = new StringBuilder();

        DAYS_OF_WEEK.stream()
                .filter(day -> dayHasLessons(lessonsByDays.get(day)))
                .forEach(day -> formatExistingEducationalDay(sb, day, lessonsByDays.get(day)));

        return sb.toString();
    }

    public String formatDay(List<LessonInfoDto> weekLessons, String weekDayName) {
        // Группируем уроки по дням
        Map<String, List<LessonInfoDto>> lessonsByDay = groupLessonsByDay(weekLessons);

        StringBuilder sb = new StringBuilder();
        List<LessonInfoDto> dayLessons = lessonsByDay.get(weekDayName);

        if (dayHasLessons(dayLessons)) {
            formatExistingEducationalDay(sb, weekDayName, dayLessons);
        } else {
            formatEmptyEducationalDay(sb, weekDayName);
        }

        return sb.toString();
    }

    private Map<String, List<LessonInfoDto>> groupLessonsByDay(List<LessonInfoDto> lessons) {
        return lessons.stream()
                .map(Mapper::toLesson)
                .filter(Lesson::isExist) // Исключаем окна(пары, которых нет) в расписании
                .map(Mapper::toLessonInfoDto)
                .collect(Collectors.groupingBy(LessonInfoDto::dateDay));
    }

    private void formatExistingEducationalDay(StringBuilder sb, String day, @NotNull List<LessonInfoDto> dayLessons) {
        String lessonDate = dayLessons.get(0).dateNumber();

        sb.append(createDayHeader(day, lessonDate));
        dayLessons.forEach(lesson -> sb.append(formatLesson(lesson)));
        sb.append("\n");
    }

    private void formatEmptyEducationalDay(StringBuilder sb, String weekDayName) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        sb.append(String.format("""
                %s %s (%s):
                Зачилься, пар нет :)
                """, DAY_HEADER, capitalizeFirstLetter(weekDayName), currentDate));
        sb.append("\n");
    }

    private String createDayHeader(String day, String lessonDate) {
        return String.format("""
                %s %s (%s):
                """, DAY_HEADER, capitalizeFirstLetter(day), lessonDate);
    }

    private String formatLesson(LessonInfoDto lesson) {
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
            return ONLINE_EMOJI;
        }

        return switch (type) {
            case "лабораторная" -> LAB_EMOJI;
            case "лекция" -> LECTURE_EMOJI;
            default -> "военная кафедра".equals(name) ? MILITARY_EMOJI : DEFAULT_EMOJI;
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
