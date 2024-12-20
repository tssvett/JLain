package dev.tssvett.schedule_bot.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotCommand {

    SendMessage execute(Long userId, Long chatId, String argument);

    default SendMessage execute(Long userId, Long chatId) {
        return execute(userId, chatId, "");
    }
}
