package dev.tssvett.schedule_bot.bot.keyboard.impl.notification;

import dev.tssvett.schedule_bot.bot.enums.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
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
public class NotificationKeyboard extends Keyboard {
    private static final Integer NOTIFICATION_ROW_SIZE = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<String> actions = List.of("Включить", "Отключить");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < actions.size(); i += NOTIFICATION_ROW_SIZE) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < NOTIFICATION_ROW_SIZE && (i + j) < actions.size(); j++) {
                String callbackInformation = actions.get(i + j);
                keyboardButtonRow.add(createButton(callbackInformation, callbackInformation, action));
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
