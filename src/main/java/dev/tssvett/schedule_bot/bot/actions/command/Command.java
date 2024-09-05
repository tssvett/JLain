package dev.tssvett.schedule_bot.actions.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {

    SendMessage execute(Long userId, Long chatId);
}
