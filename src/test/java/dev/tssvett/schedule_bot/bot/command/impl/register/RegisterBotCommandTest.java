package dev.tssvett.schedule_bot.bot.command.impl.register;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.keyboard.impl.refresh.RefreshRegistrationKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@ExtendWith(MockitoExtension.class)
class RegisterBotCommandTest {
    @Mock
    StudentService studentService;

    @Mock
    RefreshRegistrationKeyboard refreshRegistrationKeyboard;

    @Mock
    FacultyKeyboard facultyKeyboard;

    @InjectMocks
    RegisterBotCommand registerBotCommand;

    @Test
    void execute_isRegistered() {
        //Arrange
        when(studentService.isRegistered(1L)).thenReturn(true);
        when(refreshRegistrationKeyboard.createInlineKeyboard(Action.REFRESH_REGISTRATION, 1L))
                .thenReturn(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow())));

        //Act
        SendMessage result = registerBotCommand.execute(1L, 1L);

        //Assert
        verify(studentService).isRegistered(1L);
        assertEquals(MessageTextConstantsUtils.ALREADY_REGISTERED_MESSAGE, result.getText());
    }

    @Test
    void execute_isNotRegistered() {
        //Arrange
        when(studentService.isRegistered(1L)).thenReturn(false);
        doNothing().when(studentService).createStudentIfNotExists(1L, 1L);
        doNothing().when(studentService).updateStudentRegistrationState(1L,
                RegistrationState.FACULTY_CHOOSING);
        when(facultyKeyboard.createInlineKeyboard(Action.FACULTY_CHOOSE, 1L))
                .thenReturn(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow())));

        //Act
        SendMessage result = registerBotCommand.execute(1L, 1L);

        //Assert
        verify(studentService).createStudentIfNotExists(1L, 1L);
        assertEquals(MessageTextConstantsUtils.REGISTER_FACULTY_CHOOSING_MESSAGE, result.getText());
    }
}