package dev.tssvett.schedule_bot.backend.dto;

import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;


public record StudentInfoDto(
        Long userId,
        Long chatId,
        Long course,
        RegistrationState registrationState,
        Long facultyId,
        Long groupId,
        Long notificationId,
        Role role
) {
}
