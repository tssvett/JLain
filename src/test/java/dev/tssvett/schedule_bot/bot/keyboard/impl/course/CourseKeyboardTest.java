package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

class CourseKeyboardTest {

    @Test
    void createInlineKeyboard() {
        //Arrange
        Action action = Action.COURSE_CHOOSE;
        Long userId = 1L;
        InlineKeyboardMarkup expectedKeyboard = getInlineKeyboardMarkup();
        CourseKeyboard courseKeyboard = new CourseKeyboard();

        //Act
        InlineKeyboardMarkup inlineKeyboard = courseKeyboard.createInlineKeyboard(action, userId);

        //Assert
        assertEquals(expectedKeyboard.getKeyboard().size(), inlineKeyboard.getKeyboard().size());
        assertEquals(expectedKeyboard.getKeyboard().get(0).get(0).getText(),
                inlineKeyboard.getKeyboard().get(0).get(0).getText());
    }

    @NotNull
    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton("1");
        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton("2");
        InlineKeyboardButton keyboardButton3 = new InlineKeyboardButton("3");
        InlineKeyboardButton keyboardButton4 = new InlineKeyboardButton("4");
        InlineKeyboardButton keyboardButton5 = new InlineKeyboardButton("5");

        InlineKeyboardRow inlineKeyboardButtons1 = new InlineKeyboardRow(keyboardButton1, keyboardButton2,
                keyboardButton3);
        InlineKeyboardRow inlineKeyboardButtons2 = new InlineKeyboardRow(keyboardButton4, keyboardButton5);

        return new InlineKeyboardMarkup(List.of(inlineKeyboardButtons1,
                inlineKeyboardButtons2));
    }
}