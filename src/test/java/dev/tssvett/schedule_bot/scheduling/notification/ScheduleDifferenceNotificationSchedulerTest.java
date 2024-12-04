package dev.tssvett.schedule_bot.scheduling.notification;

import dev.tssvett.schedule_bot.backend.client.TelegramClientService;
import dev.tssvett.schedule_bot.backend.service.NotificationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class ScheduleDifferenceNotificationSchedulerTest {
    @Mock
    TelegramClientService telegramClientService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    ScheduleDifferenceNotificationScheduler scheduleDifferenceNotificationScheduler;

    @Test
    void sendScheduleNotificationsToUsers() {

        //Arrange
        List<SendMessage> messages = new ArrayList<>();
        messages.add(new SendMessage("123", "test"));
        when(notificationService.createScheduleDifferenceNotificationsMessages()).thenReturn(messages);
        doNothing().when(telegramClientService).sendMessageList(messages);

        //Act
        scheduleDifferenceNotificationScheduler.sendScheduleNotificationsToUsers();

        //Assert
        verify(notificationService).createScheduleDifferenceNotificationsMessages();
        verify(telegramClientService).sendMessageList(messages);
    }
}