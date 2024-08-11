package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final RegistrationService registrationService;
    @Override
    public SendMessage execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        log.info("Received " + this.getClass().getSimpleName() +  " from userId: {}", userId);
        registrationService.startCommandCallback(userId, chatId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.START_COMMAND)
                .build();
    }
}
