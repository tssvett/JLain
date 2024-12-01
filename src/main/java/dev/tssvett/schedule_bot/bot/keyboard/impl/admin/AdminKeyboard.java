package dev.tssvett.schedule_bot.bot.keyboard.impl.admin;

import dev.tssvett.schedule_bot.bot.enums.command.AdminAllowedCommands;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import java.util.List;
import java.util.stream.IntStream;
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
        return IntStream.iterate(0, i -> i < values.length, i -> i + ADMIN_KEYS_IN_ROW)
                .mapToObj(i -> createRow(i, values, action))
                .filter(row -> !row.isEmpty())
                .toList();
    }

    private InlineKeyboardRow createRow(int startIndex, AdminAllowedCommands[] values, Action action) {
        return new InlineKeyboardRow(
                IntStream.iterate(0, i -> (i < ADMIN_KEYS_IN_ROW && (startIndex + i) < values.length), i -> i + 1)
                        .mapToObj(i -> createButton(values[startIndex + i].getCommandName(),
                                values[startIndex + i].getCommandName(), action))
                        .toList()
        );
    }
}
