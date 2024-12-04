package dev.tssvett.schedule_bot.scheduling.messages;

import dev.tssvett.schedule_bot.backend.client.TelegramClientService;
import dev.tssvett.schedule_bot.backend.service.MessageService;
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
class MessagesSenderSchedulerTest {

    @Mock
    TelegramClientService telegramClientService;

    @Mock
    MessageService messageService;

    @InjectMocks
    MessagesSenderScheduler messagesSenderScheduler;

    @Test
    void sendMessages() {
        //Arrange
        List<SendMessage> messages = new ArrayList<>();
        messages.add(new SendMessage("123", "test"));
        when(messageService.findAllMessages()).thenReturn(messages);
        doNothing().when(telegramClientService).sendMessageList(messages);
        doNothing().when(messageService).removeAllMessages();

        //Act
        messagesSenderScheduler.sendMessages();

        //Assert
        verify(messageService).findAllMessages();
        verify(messageService).removeAllMessages();
        verify(telegramClientService).sendMessageList(messages);
    }
}