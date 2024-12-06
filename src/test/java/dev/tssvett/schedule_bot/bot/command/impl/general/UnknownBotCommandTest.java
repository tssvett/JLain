package dev.tssvett.schedule_bot.bot.command.impl.general;

import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

class UnknownBotCommandTest {

    @Test
    void execute_happyPath() {
        //Arrange
        UnknownBotCommand command = new UnknownBotCommand();

        //Act
        SendMessage execute = command.execute(1L, 1L);

        //Assert
        assertEquals(MessageTextConstantsUtils.UNAVAILABLE_COMMAND, execute.getText());
    }
}