package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

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
public class CourseKeyboard extends Keyboard {
    private final List<Integer> courses = List.of(1, 2, 3, 4, 5);
    private static final int COURSE_KEYS_IN_ROW = 3;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        return new InlineKeyboardMarkup(
                IntStream.iterate(0, i -> i < courses.size(), i -> i + COURSE_KEYS_IN_ROW)
                        .mapToObj(i -> createRow(i, action))
                        .toList()
        );
    }

    private InlineKeyboardRow createRow(int startIndex, Action action) {
        return new InlineKeyboardRow(
                IntStream.iterate(0, i -> (i < COURSE_KEYS_IN_ROW && (startIndex + i) < courses.size()),
                                i -> i + 1)
                        .mapToObj(i -> createButton(String.valueOf(courses.get(startIndex + i)),
                                String.valueOf(courses.get(startIndex + i)), action))
                        .toList()
        );
    }
}
