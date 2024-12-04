package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.admin.AdminKeyboard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class AdminCommandTest {
    @Mock
    AdminKeyboard adminKeyboard;

    @InjectMocks
    AdminCommand adminCommand;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute() {
        //Arrange
        when(adminKeyboard.createInlineKeyboard(Action.ADMIN_COMMAND_SELECTION, 123L))
                .thenReturn(new AdminKeyboard().createInlineKeyboard(Action.ADMIN_COMMAND_SELECTION, 123L));
        //Act
        SendMessage sendMessage = adminCommand.execute(123L, 456L, "test");
        //Assert
        assertNotNull(sendMessage);
        assertEquals("⚙ Доступные команды для админа ⚙\n", sendMessage.getText());
    }
}