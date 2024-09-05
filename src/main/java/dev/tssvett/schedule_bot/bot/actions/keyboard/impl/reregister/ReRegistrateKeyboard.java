package dev.tssvett.schedule_bot.actions.keyboard.impl.reregister;

import dev.tssvett.schedule_bot.actions.keyboard.Keyboard;
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
public class ReRegistrateKeyboard extends Keyboard {

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<String> answers = List.of("Да", "Нет");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String answer : answers) {
            rows.add(List.of(createButton(answer, answer, action)));
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
