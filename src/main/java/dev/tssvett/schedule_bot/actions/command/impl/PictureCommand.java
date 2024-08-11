package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class PictureCommand implements Command {

    @Override
    public SendMessage execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.PICTURE_COMMAND)
                .build();
    }
}
