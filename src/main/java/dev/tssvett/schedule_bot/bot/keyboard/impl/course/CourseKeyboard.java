package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

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
public class CourseKeyboard extends Keyboard {
    private final List<Integer> courses = List.of(1, 2, 3, 4, 5);
    private static final int COURSE_KEYS_IN_ROW = 3;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int i = 0; i < courses.size(); i += COURSE_KEYS_IN_ROW) {
            rows.add(createRow(i, action));
        }

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow createRow(int startIndex, Action action) {
        InlineKeyboardRow keyboardButtonRow = new InlineKeyboardRow();

        for (int j = 0; j < COURSE_KEYS_IN_ROW && (startIndex + j) < courses.size(); j++) {
            String callbackInformation = String.valueOf(courses.get(startIndex + j));
            keyboardButtonRow.add(createButton(callbackInformation, callbackInformation, action));
        }

        return keyboardButtonRow;
    }
}