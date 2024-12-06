package dev.tssvett.schedule_bot.backend.client;

import dev.tssvett.schedule_bot.bot.handler.provider.CallbackProvider;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@ExtendWith(MockitoExtension.class)
class TelegramClientServiceTest {

    @Mock
    private TelegramClient telegramClient;

    @Mock
    private CallbackProvider callbackProvider;

    @InjectMocks
    private TelegramClientService telegramClientService;

    CallbackQuery callbackQuery;
    Update update;
    SendMessage sendMessage;


    @BeforeEach
    void setUp() {
        callbackQuery = new CallbackQuery();
        callbackQuery.setData("test");

        update = new Update();
        update.setCallbackQuery(callbackQuery);

        sendMessage = new SendMessage("1245", "test");
    }

    @Test
    void handleUpdate_notNull_doesNotThrow() throws TelegramApiException {
        //Arrange
        when(telegramClient.execute(sendMessage)).thenReturn(new Message());
        when(callbackProvider.handleMessage(update)).thenReturn(sendMessage);

        //Act && Assert
        Assertions.assertDoesNotThrow(() -> telegramClientService.handleUpdate(update));
    }

    @Test
    void sendMessageList_notNull_doesNotThrow() throws TelegramApiException {
        //Arrange
        List<SendMessage> messages = List.of(new SendMessage("1245", "test"));

        //Act
        when(telegramClient.execute(sendMessage)).thenReturn(new Message());

        //Assert
        Assertions.assertDoesNotThrow(() -> telegramClientService.sendMessageList(messages));
    }
}