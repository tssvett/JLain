package dev.tssvett.schedule_bot.actions.keyboard;

import dev.tssvett.schedule_bot.enums.Action;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Keyboard {

    InlineKeyboardMarkup createInlineKeyboard(Action action);
}
