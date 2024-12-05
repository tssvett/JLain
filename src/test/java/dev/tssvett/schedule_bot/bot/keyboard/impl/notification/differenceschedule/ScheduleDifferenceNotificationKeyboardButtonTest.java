package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@ExtendWith(MockitoExtension.class)
class ScheduleDifferenceNotificationKeyboardButtonTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    ScheduleDifferenceNotificationKeyboardButton scheduleDifferenceNotificationKeyboardButton;

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
    void onButtonClick_happyPath_true() {
        //Arrange
        callbackQuery.setData("SCHEDULE_DIFFERENCE_NOTIFICATION Включить");
        doNothing().when(studentService).updateScheduleDifferenceNotificationStatus(123L, true);

        //Act
        SendMessage sendMessage = scheduleDifferenceNotificationKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals("Уведомления для разницы включены", sendMessage.getText());
    }

    @Test
    void onButtonClick_happyPath_false() {
        //Arrange
        callbackQuery.setData("SCHEDULE_DIFFERENCE_NOTIFICATION Выключить");
        doNothing().when(studentService).updateScheduleDifferenceNotificationStatus(123L,
                false);

        //Act
        SendMessage sendMessage = scheduleDifferenceNotificationKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals("Уведомления для разницы отключены", sendMessage.getText());
    }
}