package dev.tssvett.schedule_bot.bot.keyboard.impl.admin;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

class AdminKeyboardTest {

    @Test
    void createInlineKeyboard() {
        //Arrange
        Action action = Action.ADMIN_COMMAND_SELECTION;
        Long userId = 1L;
        AdminKeyboard adminKeyboard = new AdminKeyboard();
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton("/show_registered_users");
        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton("/send_message_to_users");
        InlineKeyboardRow inlineKeyboardButtons = new InlineKeyboardRow(keyboardButton1, keyboardButton2);
        InlineKeyboardMarkup expectedKeyboard = new InlineKeyboardMarkup(List.of(inlineKeyboardButtons));

        //Act
        InlineKeyboardMarkup inlineKeyboard = adminKeyboard.createInlineKeyboard(action, userId);

        //Assert
        assertEquals(expectedKeyboard.getKeyboard().size(), inlineKeyboard.getKeyboard().size());
        assertEquals(expectedKeyboard.getKeyboard().get(0).get(0).getText(), inlineKeyboard.getKeyboard().get(0).get(0).getText());
    }
}