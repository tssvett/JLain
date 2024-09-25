package dev.tssvett.schedule_bot.bot.formatter;

import dev.tssvett.schedule_bot.backend.entity.Lesson;
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

    public String formatWeek(List<Lesson> weekLessons) {
        StringBuilder sb = new StringBuilder();

        // Группируем уроки по дням
        Map<String, List<Lesson>> lessonsByDay = groupLessonsByDay(weekLessons);

        // Определяем порядок дней недели
        List<String> daysOfWeek = List.of("понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье");

        for (String day : daysOfWeek) {
            List<Lesson> dayLessons = lessonsByDay.get(day);
            if (dayLessons != null && !dayLessons.isEmpty()) {
                appendDaySchedule(sb, day, dayLessons);
            }
        }

        return sb.toString();
    }

    public String formatDay(List<Lesson> weekLessons, String weekDayName) {
        StringBuilder sb = new StringBuilder();
        if (weekDayName.equals("воскресенье")) {
            weekDayName = "понедельник";
        }

        // Группируем уроки по дням
        Map<String, List<Lesson>> lessonsByDay = groupLessonsByDay(weekLessons);

        List<Lesson> dayLessons = lessonsByDay.get(weekDayName);
        if (dayLessons != null && !dayLessons.isEmpty()) {
            appendDaySchedule(sb, weekDayName, dayLessons);
        }

        return sb.toString();
    }

    private Map<String, List<Lesson>> groupLessonsByDay(List<Lesson> lessons) {
        return lessons.stream()
                .filter(Lesson::isExist) // Исключаем пары, которых нет
                .collect(Collectors.groupingBy(Lesson::getDateDay));
    }

    private void appendDaySchedule(StringBuilder sb, String day, List<Lesson> dayLessons) {
        String dateNumber = dayLessons.getFirst().getDateNumber(); // Получаем дату для вывода
        sb.append("🔹 ").append(capitalizeFirstLetter(day)).append(" (").append(dateNumber).append("):\n");

        for (Lesson lesson : dayLessons) {
            sb.append(formatLesson(lesson));
        }

        sb.append("\n"); // Добавляем пустую строку для разделения дней
    }

    private String formatLesson(Lesson lesson) {
        String emoji = getEmojiForLesson(lesson);

        return String.format(
                "%s %s\n" + // Смайлик и название пары
                        "Тип: %s\n" +
                        "Место: %s\n" +
                        "Время: %s\n\n",
                emoji,
                capitalizeFirstLetter(lesson.getName()),
                lesson.getType(),
                lesson.getPlace(),
                lesson.getTime()
        );
    }

    private String getEmojiForLesson(Lesson lesson) {
        if (lesson.getPlace().equalsIgnoreCase("online")) {
            return ONLINE_EMOJI;
        } else if (lesson.getType().equalsIgnoreCase("лабораторная")) {
            return LAB_EMOJI;
        } else if (lesson.getType().equalsIgnoreCase("лекция")) {
            return LECTURE_EMOJI;
        } else if (lesson.getName().equalsIgnoreCase("Военная кафедра")) {
            lesson.setType("Военное"); // Устанавливаем тип "Военное"
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
