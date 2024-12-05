package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.NO_RE_REGISTRATION_ANSWER;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
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
class RefreshRegistrationSelectionKeyboardButtonTest {

    @Mock
    StudentService studentService;

    @Mock
    FacultyKeyboard facultyKeyboard;

    @InjectMocks
    RefreshRegistrationSelectionKeyboardButton refreshRegistrationSelectionKeyboardButton;

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
    void onButtonClick_answerYes() {
        //Arrange
        callbackQuery.setData("REFRESH_REGISTRATION Да");
        doNothing().when(studentService).updateStudentRegistrationState(123L,
                RegistrationState.FACULTY_CHOOSING);
        when(facultyKeyboard.createInlineKeyboard(Action.FACULTY_CHOOSE, 123L))
                .thenReturn(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow())));

        //Act
        SendMessage sendMessage = refreshRegistrationSelectionKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals(MessageTextConstantsUtils.REGISTER_FACULTY_CHOOSING_MESSAGE, sendMessage.getText());
    }

    @Test
    void onButtonClick_answerNo() {
        //Arrange
        callbackQuery.setData("REFRESH_REGISTRATION Нет");

        //Act
        SendMessage sendMessage = refreshRegistrationSelectionKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals(NO_RE_REGISTRATION_ANSWER, sendMessage.getText());
    }
}
