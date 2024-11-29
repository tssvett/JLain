package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleDifferenceNotificationKeyboard extends Keyboard {
    private static final int NOTIFICATION_ROW_SIZE = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<String> actions = List.of("Включить", "Отключить");
        List<InlineKeyboardRow> rows = createRows(actions, action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardRow> createRows(List<String> actions, Action action) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int i = 0; i < actions.size(); i += NOTIFICATION_ROW_SIZE) {
            InlineKeyboardRow keyboardButtonRow = createRow(i, actions, action);
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }

        return rows;
    }

    private InlineKeyboardRow createRow(int startIndex, List<String> actions, Action action) {
        InlineKeyboardRow keyboardButtonRow = new InlineKeyboardRow();

        for (int j = 0; j < NOTIFICATION_ROW_SIZE && (startIndex + j) < actions.size(); j++) {
            String callbackInformation = actions.get(startIndex + j);
            keyboardButtonRow.add(createButton(callbackInformation, callbackInformation, action));
        }

        return keyboardButtonRow;
    }
}