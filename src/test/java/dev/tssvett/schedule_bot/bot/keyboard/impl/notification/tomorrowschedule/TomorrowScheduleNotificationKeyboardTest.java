package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.tomorrowschedule;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

class TomorrowScheduleNotificationKeyboardTest {

    @Test
    void createInlineKeyboard() {
        //Arrange
        Action action = Action.TOMORROW_SCHEDULE_NOTIFICATION;
        Long userId = 1L;
        InlineKeyboardMarkup expectedKeyboard = getInlineKeyboardMarkup();
        TomorrowScheduleNotificationKeyboard scheduleDifferenceNotificationKeyboard
                = new TomorrowScheduleNotificationKeyboard();


        //Act
        InlineKeyboardMarkup inlineKeyboard = scheduleDifferenceNotificationKeyboard
                .createInlineKeyboard(action, userId);

        //Assert
        assertEquals(expectedKeyboard.getKeyboard().size(), inlineKeyboard.getKeyboard().size());
        assertEquals(expectedKeyboard.getKeyboard().get(0).get(0).getText(),
                inlineKeyboard.getKeyboard().get(0).get(0).getText());
    }

    @NotNull
    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton("Включить");
        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton("Выключить");

        InlineKeyboardRow inlineKeyboardButtons1 = new InlineKeyboardRow(keyboardButton1, keyboardButton2);

        return new InlineKeyboardMarkup(List.of(inlineKeyboardButtons1));
    }
}
