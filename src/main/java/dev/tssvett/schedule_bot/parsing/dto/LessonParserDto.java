package dev.tssvett.schedule_bot.parsing.dto;

import java.util.UUID;

public record LessonParserDto(
        UUID id,
        String name,
        String type,
        String place,
        String teacher,
        String subgroup,
        String time,
        String dateDay,
        String dateNumber,
        Long groupId
) {
}
