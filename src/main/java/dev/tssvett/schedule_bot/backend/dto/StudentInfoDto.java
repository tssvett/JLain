package dev.tssvett.schedule_bot.backend.dto;

import dev.tssvett.schedule_bot.bot.enums.RegistrationState;

import javax.validation.constraints.NotNull;


public record StudentInfoDto(
        @NotNull Long userId,
        @NotNull Long chatId,
        Long course,
        RegistrationState registrationState,
        FacultyInfoDto faculty,
        GroupInfoDto group,
        NotificationInfoDto notification
) {
}
