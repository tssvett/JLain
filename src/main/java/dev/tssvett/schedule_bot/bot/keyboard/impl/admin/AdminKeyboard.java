package dev.tssvett.schedule_bot.bot.keyboard.impl.admin;

import dev.tssvett.schedule_bot.bot.enums.command.AdminAllowedCommands;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class AdminKeyboard extends Keyboard {
    public static final int ADMIN_KEYS_IN_ROW = 3;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<InlineKeyboardRow> rows = createRows(AdminAllowedCommands.values(), action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardRow> createRows(AdminAllowedCommands[] values, Action action) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int i = 0; i < values.length; i += ADMIN_KEYS_IN_ROW) {
            InlineKeyboardRow keyboardButtonRow = createRow(i, values, action);
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }

        return rows;
    }

    private InlineKeyboardRow createRow(int startIndex, AdminAllowedCommands[] values, Action action) {
        InlineKeyboardRow keyboardButtonRow = new InlineKeyboardRow();

        for (int j = 0; j < ADMIN_KEYS_IN_ROW && (startIndex + j) < values.length; j++) {
            AdminAllowedCommands adminCommand = values[startIndex + j];
            keyboardButtonRow.add(createButton(adminCommand.getCommandName(), adminCommand.getCommandName(), action));
        }

        return keyboardButtonRow;
    }
}
