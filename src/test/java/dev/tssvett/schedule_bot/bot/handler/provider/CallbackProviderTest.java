package dev.tssvett.schedule_bot.bot.handler.provider;

import dev.tssvett.schedule_bot.bot.handler.CommandHandler;
import dev.tssvett.schedule_bot.bot.handler.KeyboardHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@ExtendWith(MockitoExtension.class)
class CallbackProviderTest {
    @Mock
    CommandHandler commandsHandler;

    @Mock
    KeyboardHandler keyboardHandler;

    @InjectMocks
    CallbackProvider callbackProvider;

    @Test
    void handleMessage_isMessage_commandHandlerInvoked() {
        //Arrange
        Update update = new Update();
        Message message = new Message();
        message.setText("test");
        update.setMessage(message);

        when(commandsHandler.handleCommands(update)).thenReturn(new SendMessage("123", "test"));

        //Act
        callbackProvider.handleMessage(update);

        //Assert
        verify(commandsHandler).handleCommands(update);
    }

    @Test
    void handleMessage_isNotMessage_keyboardHandlerInvoked() {

        //Arrange
        Update update = new Update();

        when(keyboardHandler.handleKeyboardAction(update)).thenReturn(new SendMessage("123", "test"));

        //Act
        callbackProvider.handleMessage(update);

        //Assert
        verify(keyboardHandler).handleKeyboardAction(update);
    }
}