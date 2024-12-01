package dev.tssvett.schedule_bot.bot.command.impl.general;

import dev.tssvett.schedule_bot.bot.annotation.NoneRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class UnknownBotCommand implements BotCommand {
    @Override
    @NoneRequired
    public SendMessage execute(Long userId, Long chatId, String commandName) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.UNAVAILABLE_COMMAND)
                .build();
    }
}