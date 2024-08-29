package dev.tssvett.schedule_bot.actions.keyboard;

import dev.tssvett.schedule_bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.enums.Action;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public abstract class Keyboard {

    public abstract InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId);

    protected void setCallbackInformation(String callbackInformation, Action action, InlineKeyboardButton keyboardButton) {
        CallbackDetails callbackDetails = CallbackDetails.builder()
                .action(action)
                .callbackInformation(callbackInformation)
                .build();
        keyboardButton.setCallbackData(callbackDetails.toString());
    }

    protected InlineKeyboardButton createButton(String buttonText, String callbackInformation, Action action) {
        InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                .text(buttonText)
                .build();
        //Передаем коллбекную информацию нажатой кнопке
        setCallbackInformation(callbackInformation, action, keyboardButton);
        return keyboardButton;
    }
}
