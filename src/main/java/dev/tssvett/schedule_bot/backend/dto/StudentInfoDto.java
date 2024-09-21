package dev.tssvett.schedule_bot.backend.dto;

import dev.tssvett.schedule_bot.backend.entity.Faculty;
import dev.tssvett.schedule_bot.backend.entity.Group;
import dev.tssvett.schedule_bot.backend.entity.Notification;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import jakarta.validation.constraints.NotNull;


public record StudentInfoDto(
        @NotNull Long userId,
        @NotNull Long chatId,
        Long course,
        RegistrationState registrationState,
        Faculty faculty,
        Group group,
        Notification notification
) {
}
