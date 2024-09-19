package dev.tssvett.schedule_bot.bot.actions.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotCommand {

    SendMessage execute(Long userId, Long chatId);
}
