package dev.tssvett.schedule_bot.backend.dto;

public record GroupInfoDto(
        Long groupId,
        String name,
        Long course,
        FacultyInfoDto faculty
) {
}
