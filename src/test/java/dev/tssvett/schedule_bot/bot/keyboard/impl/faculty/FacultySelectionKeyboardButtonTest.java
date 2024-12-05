package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.course.CourseKeyboard;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.FACULTY_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.REGISTER_CHOOSE_COURSE_MESSAGE;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@ExtendWith(MockitoExtension.class)
class FacultySelectionKeyboardButtonTest {

    @Mock
    StudentService studentService;

    @Mock
    CourseKeyboard courseKeyboard;

    @InjectMocks
    FacultySelectionKeyboardButton facultySelectionKeyboardButton;

    private Update update;
    private CallbackQuery callbackQuery;

    @BeforeEach
    void setup() {
        update = new Update();
        callbackQuery = new CallbackQuery();
        User from = new User(123L, "123", false);
        callbackQuery.setFrom(from);
        Message message = new Message();
        Chat chat = new Chat(123L, "type");
        message.setChat(chat);
        callbackQuery.setMessage(message);
        update.setCallbackQuery(callbackQuery);
    }

    @Test
    void onButtonClick_happyPath() {
        //Arrange
        callbackQuery.setData("FACULTY_CHOOSE 123");
        doNothing().when(studentService).updateStudentFaculty(123L, 123L);
        when(courseKeyboard.createInlineKeyboard(Action.COURSE_CHOOSE, 123L))
                .thenReturn(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow())));

        //Act
        SendMessage sendMessage = facultySelectionKeyboardButton.onButtonClick(update);

        //Assert
        verify(courseKeyboard).createInlineKeyboard(Action.COURSE_CHOOSE, 123L);
        assertEquals(REGISTER_CHOOSE_COURSE_MESSAGE, sendMessage.getText());
    }

    @Test
    void onButtonClick_exceptionThrows() {
        //Arrange
        callbackQuery.setData("FACULTY_CHOOSE 123");
        doThrow(NotValidRegistrationStateException.class)
                .when(studentService).updateStudentFaculty(123L, 123L);
        //Act
        SendMessage sendMessage = facultySelectionKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals(FACULTY_CLICK_WITH_ERROR_STATE, sendMessage.getText());
    }
}