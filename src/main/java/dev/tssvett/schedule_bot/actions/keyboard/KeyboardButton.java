package dev.tssvett.schedule_bot.actions.keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface KeyboardButtonCallback {

    SendMessage callback(Update update);
}
