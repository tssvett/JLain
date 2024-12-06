package dev.tssvett.schedule_bot.bot.keyboard.impl.admin;

import dev.tssvett.schedule_bot.bot.command.impl.admin.HelpSendMessageCommand;
import dev.tssvett.schedule_bot.bot.command.impl.admin.ShowRegisteredStudentsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@ExtendWith(MockitoExtension.class)
class AdminCommandSelectionKeyboardButtonTest {
    @Mock
    ShowRegisteredStudentsCommand showRegisteredStudentsCommand;

    @Mock
    HelpSendMessageCommand helpSendMessageCommand;

    AdminCommandSelectionKeyboardButton adminCommandSelectionKeyboardButton;

    private Update update;
    private CallbackQuery callbackQuery;

    @BeforeEach
    void setup() {
        adminCommandSelectionKeyboardButton = new AdminCommandSelectionKeyboardButton(showRegisteredStudentsCommand,
                helpSendMessageCommand);
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
    void onButtonClick_Show_Registered() {
        //Arrange
        callbackQuery.setData("ADMIN_COMMAND_SELECTION /show_registered_users");
        update.setCallbackQuery(callbackQuery);
        SendMessage sendMessage = new SendMessage("123", "text");
        when(showRegisteredStudentsCommand.execute(123L, 123L))
                .thenReturn(sendMessage);

        //Act
        adminCommandSelectionKeyboardButton.onButtonClick(update);

        //Assert
        verify(showRegisteredStudentsCommand).execute(123L, 123L);
    }

    @Test
    void onButtonClick_helpSendMessage() {
        //Arrange
        callbackQuery.setData("ADMIN_COMMAND_SELECTION /send_message_to_users");
        update.setCallbackQuery(callbackQuery);
        SendMessage sendMessage = new SendMessage("123", "text");
        when(helpSendMessageCommand.execute(123L, 123L))
                .thenReturn(sendMessage);

        //Act
        adminCommandSelectionKeyboardButton.onButtonClick(update);

        //Assert
        verify(helpSendMessageCommand).execute(123L, 123L);
    }
}