package dev.tssvett.schedule_bot.backend.dto;

import dev.tssvett.schedule_bot.bot.enums.LessonType;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;

import java.util.UUID;

public record LessonInfoDto(
        UUID lessonId,
        String name,
        LessonType type,
        String place,
        String teacher,
        Subgroup subgroup,
        String time,
        String dateDay,
        String dateNumber,
        Long groupId
) {
    public boolean isExist() {
        return name != null && !name.isEmpty() && type != null && !type.getName().isEmpty();
    }
}
