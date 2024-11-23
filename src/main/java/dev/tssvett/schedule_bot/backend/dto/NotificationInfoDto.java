package dev.tssvett.schedule_bot.backend.dto;

public record NotificationInfoDto(
        Long notificationId,
        Boolean tomorrowScheduleEnabled,
        Boolean scheduleDifferenceEnabled,
        Long studentId
) {
}
