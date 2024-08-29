package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.NoneRequired;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final UserService userService;

    @Override
    @NoneRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        BotUser botUser = userService.createUserIfNotExistForStartCommand(userId, chatId);
        log.info("User {} registration state: {}", userId, botUser.getRegistrationState());
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.START_COMMAND)
                .build();
    }
}
