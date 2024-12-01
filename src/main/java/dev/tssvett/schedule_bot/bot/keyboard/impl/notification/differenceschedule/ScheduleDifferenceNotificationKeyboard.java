package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import java.util.List;
import java.util.stream.IntStream;
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
        return IntStream.iterate(0, i -> i < actions.size(), i -> i + NOTIFICATION_ROW_SIZE)
                .mapToObj(i -> createRow(i, actions, action))
                .filter(row -> !row.isEmpty())
                .toList();
    }

    private InlineKeyboardRow createRow(int startIndex, List<String> actions, Action action) {
        return new InlineKeyboardRow(
                IntStream.iterate(0, i -> (i < NOTIFICATION_ROW_SIZE && (startIndex + i) < actions.size()),
                                i -> i + 1)
                        .mapToObj(i -> createButton(actions.get(startIndex + i), actions.get(startIndex + i), action))
                        .toList()
        );
    }
}