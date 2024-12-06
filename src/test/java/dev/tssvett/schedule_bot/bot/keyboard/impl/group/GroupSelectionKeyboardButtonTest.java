package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.GROUP_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.SUCCESSFULLY_REGISTERED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;


@ExtendWith(MockitoExtension.class)
class GroupSelectionKeyboardButtonTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    GroupSelectionKeyboardButton groupSelectionKeyboardButton;

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
        callbackQuery.setData("GROUP_CHOOSE 123");
        doNothing().when(studentService).updateStudentGroup(123L, 123L);

        //Act
        SendMessage sendMessage = groupSelectionKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals(SUCCESSFULLY_REGISTERED_MESSAGE, sendMessage.getText());
    }

    @Test
    void onButtonClick_exceptionThrows() {
        //Arrange
        callbackQuery.setData("GROUP_CHOOSE 123");
        doThrow(NotValidRegistrationStateException.class)
                .when(studentService).updateStudentGroup(123L, 123L);
        //Act
        SendMessage sendMessage = groupSelectionKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals(GROUP_CLICK_WITH_ERROR_STATE, sendMessage.getText());
    }
}