package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

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
public class RefreshRegistrationKeyboard extends Keyboard {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<String> answers = List.of("Да", "Нет");
        List<InlineKeyboardRow> rows = createRows(answers, action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardRow> createRows(List<String> answers, Action action) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (String answer : answers) {
            rows.add(new InlineKeyboardRow(createButton(answer, answer, action)));
        }

        return rows;
    }
}