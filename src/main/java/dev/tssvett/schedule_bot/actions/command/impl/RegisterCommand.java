package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterCommand implements Command {
    private final RegistrationService registrationService;

    @Override
    @DirectMessageRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return registrationService.registerUserCommandCallback(userId, chatId);
    }
}
