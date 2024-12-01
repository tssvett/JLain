package dev.tssvett.schedule_bot.bot.command.impl.general;

import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class PictureBotCommand implements BotCommand {

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId, String argument) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.PICTURE_COMMAND)
                .build();
    }
}
