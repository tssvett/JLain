package dev.tssvett.schedule_bot.backend.dto;

import java.util.UUID;

public record LessonInfoDto(
        UUID lessonId,
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
