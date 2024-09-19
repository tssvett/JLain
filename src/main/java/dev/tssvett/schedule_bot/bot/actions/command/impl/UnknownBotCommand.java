package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.NoneRequired;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class UnknownBotCommand implements BotCommand {
    @Override
    @NoneRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.UNAVAILABLE_COMMAND)
                .build();
    }
}
