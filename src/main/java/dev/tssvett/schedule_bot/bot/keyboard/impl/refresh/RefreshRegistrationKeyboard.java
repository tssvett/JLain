package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
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
public class RefreshRegistrationKeyboard extends Keyboard {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<String> answers = List.of("Да", "Нет");
        List<List<InlineKeyboardButton>> rows = createRows(answers, action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<List<InlineKeyboardButton>> createRows(List<String> answers, Action action) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (String answer : answers) {
            rows.add(List.of(createButton(answer, answer, action)));
        }

        return rows;
    }
}