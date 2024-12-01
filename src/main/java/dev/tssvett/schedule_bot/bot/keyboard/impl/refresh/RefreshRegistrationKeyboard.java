package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
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
    private static final List<String> answers = List.of("Да", "Нет");

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        return new InlineKeyboardMarkup(createRows(action));
    }

    private List<InlineKeyboardRow> createRows(Action action) {
        return answers.stream()
                .map(answer -> new InlineKeyboardRow(createButton(answer, answer, action)))
                .toList();
    }
}