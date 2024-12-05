package dev.tssvett.schedule_bot.bot.command.impl.general;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class StartBotCommandTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    StartBotCommand startBotCommand;

    @Test
    void execute_happyPath() {
        //Arrange
        doNothing().when(studentService).createStudentIfNotExists(1L, 1L);
        startBotCommand = new StartBotCommand(studentService);

        //Act
        SendMessage result = startBotCommand.execute(1L, 1L);

        //Assert
        verify(studentService).createStudentIfNotExists(1L, 1L);
        assertEquals(MessageTextConstantsUtils.START_COMMAND, result.getText());
    }
}