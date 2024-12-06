package dev.tssvett.schedule_bot.bot.command.impl.general;

import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.HELP_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

class HelpBotCommandTest {

    @Test
    void execute_happyPath() {
        //Arrange
        HelpBotCommand helpBotCommand = new HelpBotCommand();

        //Act
        SendMessage sendMessage = helpBotCommand.execute(1L, 1L, "test");

        //Assert
        assertNotNull(sendMessage);
        assertEquals(HELP_COMMAND, sendMessage.getText());
    }
}