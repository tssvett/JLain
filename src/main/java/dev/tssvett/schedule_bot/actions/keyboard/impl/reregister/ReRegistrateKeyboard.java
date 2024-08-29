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
public class ReRegistrateKeyboard implements Keyboard {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action) {
        List<String> answers = List.of("Да", "Нет");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String answer : answers) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText(answer);
            CallbackDetails callbackDetails = CallbackDetails.builder()
                    .action(action)
                    //УСТАНОВКА ответа КАК ТЕКСТ КОЛБЕКА
                    .callbackText(answer)
                    .build();
            keyboardButton.setCallbackData(callbackDetails.toString());
            rows.add(List.of(keyboardButton));
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
