package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.bot.actions.command.Command;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class PictureCommand implements Command {

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received {} from userId: {}", this.getClass().getSimpleName(), userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.PICTURE_COMMAND)
                .build();
    }
}
