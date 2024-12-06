package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.tomorrowschedule;

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
class TomorrowScheduleNotificationKeyboardButtonTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    TomorrowScheduleNotificationKeyboardButton tomorrowScheduleNotificationKeyboardButton;

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
        callbackQuery.setData("TOMORROW_SCHEDULE_NOTIFICATION Включить");
        doNothing().when(studentService).updateTomorrowScheduleNotificationStatus(123L, true);

        //Act
        SendMessage sendMessage = tomorrowScheduleNotificationKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals("Уведомления для ежедневного расписания включены", sendMessage.getText());
    }

    @Test
    void onButtonClick_happyPath_false() {
        //Arrange
        callbackQuery.setData("TOMORROW_SCHEDULE_NOTIFICATION Выключить");
        doNothing().when(studentService).updateTomorrowScheduleNotificationStatus(123L,
                false);

        //Act
        SendMessage sendMessage = tomorrowScheduleNotificationKeyboardButton.onButtonClick(update);

        //Assert
        assertEquals("Уведомления для ежедневного расписания отключены", sendMessage.getText());
    }
}