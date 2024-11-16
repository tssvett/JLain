package dev.tssvett.schedule_bot.parsing.dto;

public record LessonParserDto(
        String name,
        String type,
        String place,
        String teacher,
        String subgroup,
        String time,
        String dateDay,
        String dateNumber
) {
}
