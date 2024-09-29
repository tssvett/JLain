package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.persistence.entity.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public String formatWeek(List<LessonInfoDto> weekLessons) {
        StringBuilder sb = new StringBuilder();

        // Группируем уроки по дням
        Map<String, List<LessonInfoDto>> lessonsByDay = groupLessonsByDay(weekLessons);

        // Определяем порядок дней недели
        List<String> daysOfWeek = List.of("понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье");

        for (String day : daysOfWeek) {
            List<LessonInfoDto> dayLessons = lessonsByDay.get(day);
            if (dayLessons != null && !dayLessons.isEmpty()) {
                appendDaySchedule(sb, day, dayLessons);
            }
        }

        return sb.toString();
    }

    public String formatDay(List<LessonInfoDto> weekLessons, String weekDayName) {
        StringBuilder sb = new StringBuilder();
        if (weekDayName.equals("воскресенье")) {
            weekDayName = "понедельник";
        }

        // Группируем уроки по дням
        Map<String, List<LessonInfoDto>> lessonsByDay = groupLessonsByDay(weekLessons);

        List<LessonInfoDto> dayLessons = lessonsByDay.get(weekDayName);
        if (dayLessons != null && !dayLessons.isEmpty()) {
            appendDaySchedule(sb, weekDayName, dayLessons);
        }

        return sb.toString();
    }

    private Map<String, List<LessonInfoDto>> groupLessonsByDay(List<LessonInfoDto> lessons) {
        return lessons.stream()
                .map(Mapper::toLesson)
                .filter(Lesson::isExist) // Исключаем пары, которых нет
                .map(Mapper::toLessonInfoDto)
                .collect(Collectors.groupingBy(LessonInfoDto::dateDay));
    }

    private void appendDaySchedule(StringBuilder sb, String day, List<LessonInfoDto> dayLessons) {
        String dateNumber = dayLessons.getFirst().dateNumber(); // Получаем дату для вывода
        sb.append("🔹 ").append(capitalizeFirstLetter(day)).append(" (").append(dateNumber).append("):\n");

        for (LessonInfoDto lesson : dayLessons) {
            sb.append(formatLesson(lesson));
        }

        sb.append("\n"); // Добавляем пустую строку для разделения дней
    }

    private String formatLesson(LessonInfoDto lesson) {
        String emoji = getEmojiForLesson(lesson);

        return String.format(
                "%s %s\n" + // Смайлик и название пары
                        "Тип: %s\n" +
                        "Место: %s\n" +
                        "Время: %s\n\n",
                emoji,
                capitalizeFirstLetter(lesson.name()),
                lesson.type(),
                lesson.place(),
                lesson.time()
        );
    }

    private String getEmojiForLesson(LessonInfoDto lesson) {
        if (lesson.place().equalsIgnoreCase("online")) {
            return ONLINE_EMOJI;
        } else if (lesson.type().equalsIgnoreCase("лабораторная")) {
            return LAB_EMOJI;
        } else if (lesson.type().equalsIgnoreCase("лекция")) {
            return LECTURE_EMOJI;
        } else if (lesson.name().equalsIgnoreCase("Военная кафедра")) {
            return MILITARY_EMOJI;
        } else {
            return DEFAULT_EMOJI;
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
