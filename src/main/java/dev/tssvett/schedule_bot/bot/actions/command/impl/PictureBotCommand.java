package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class PictureBotCommand implements BotCommand {

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.PICTURE_COMMAND)
                .build();
    }
}
