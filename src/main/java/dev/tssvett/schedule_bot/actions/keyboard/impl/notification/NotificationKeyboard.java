package dev.tssvett.schedule_bot.actions.keyboard.impl;

import dev.tssvett.schedule_bot.actions.keyboard.Keyboard;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.enums.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKeyboard implements Keyboard {
    private static final Integer NOTIFICATION_ROW_SIZE = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action) {
        List<String> actions = List.of("Включить", "Отключить");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < actions.size(); i += NOTIFICATION_ROW_SIZE) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < NOTIFICATION_ROW_SIZE && (i + j) < actions.size(); j++) {
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                keyboardButton.setText(actions.get(i + j));
                //Настройка коллбейка для кнопки
                //Решено использовать название команды, которая вызвала клавиатуру
                CallbackDetails callbackDetails = CallbackDetails.builder()
                        .action(action)
                        //УСТАНОВКА строки как текста коллбека
                        .callbackText(actions.get(i + j))
                        .build();
                keyboardButton.setCallbackData(callbackDetails.toString());
                keyboardButtonRow.add(keyboardButton);
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
