package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterCommand implements Command {
    private final RegistrationService registrationService;

    @Override
    public SendMessage execute(Update update) {

        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return registrationService.registerUserCommandCallback(userId, chatId);
    }
}
