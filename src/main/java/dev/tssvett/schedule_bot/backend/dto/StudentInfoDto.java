package dev.tssvett.schedule_bot.backend.dto;

import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;
import javax.validation.constraints.NotNull;


public record StudentInfoDto(
        @NotNull Long userId,
        @NotNull Long chatId,
        Long course,
        RegistrationState registrationState,
        Long facultyId,
        Long groupId,
        Long notificationId,
        Role role
) {
}
