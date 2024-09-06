package dev.tssvett.schedule_bot.bot.actions.keyboard.impl.course;

import dev.tssvett.schedule_bot.bot.actions.keyboard.Keyboard;
import dev.tssvett.schedule_bot.bot.enums.Action;
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
public class CourseKeyboard extends Keyboard {
    private static final Integer COURSE_KEYS_IN_ROW = 3;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<Integer> courses = List.of(1, 2, 3, 4, 5);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < courses.size(); i += COURSE_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < COURSE_KEYS_IN_ROW && (i + j) < courses.size(); j++) {
                Integer course = courses.get(i + j);
                String callbackInformation = course.toString();
                keyboardButtonRow.add(createButton(course.toString(), callbackInformation, action));
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
