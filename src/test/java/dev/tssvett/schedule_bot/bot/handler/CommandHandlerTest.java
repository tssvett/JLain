package dev.tssvett.schedule_bot.bot.handler;

import dev.tssvett.schedule_bot.bot.command.BotCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
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
class CommandHandlerTest {

    @Mock
    private BotCommand startBotCommand;

    @Mock
    private BotCommand helpBotCommand;

    @Mock
    private BotCommand todayScheduleBotCommand;

    @Mock
    private BotCommand tomorrowScheduleBotCommand;

    @Mock
    private BotCommand weekScheduleBotCommand;

    @Mock
    private BotCommand registerBotCommand;

    @Mock
    private BotCommand unknownBotCommand;

    @Mock
    private BotCommand infoBotCommand;

    @Mock
    private BotCommand tomorrowScheduleNotificationSettingsCommand;

    @Mock
    private BotCommand differenceScheduleNotificationSettingsCommand;

    @Mock
    private BotCommand showRegisteredStudentsCommand;

    @Mock
    private BotCommand adminCommand;

    @Mock
    private BotCommand sendMessageToUsersCommand;

    private CommandHandler commandHandler;

    private Update update;

    private SendMessage expectedMessage;

    @BeforeEach
    void setUp() {
        update = new Update();
        Message message = new Message();
        message.setChat(new Chat(456L, "type"));
        message.setFrom(new User(123L, "username", false));
        update.setMessage(message);
        update.setCallbackQuery(new CallbackQuery());
        expectedMessage = new SendMessage("123", "Welcome!");
        commandHandler = new CommandHandler(
                startBotCommand,
                helpBotCommand,
                todayScheduleBotCommand,
                tomorrowScheduleBotCommand,
                weekScheduleBotCommand,
                registerBotCommand,
                unknownBotCommand,
                infoBotCommand,
                tomorrowScheduleNotificationSettingsCommand,
                differenceScheduleNotificationSettingsCommand,
                showRegisteredStudentsCommand,
                adminCommand,
                sendMessageToUsersCommand
        );
    }

    @Test
    void handleStartCommand() {
        update.getMessage().setText("/start");
        when(startBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(startBotCommand).execute(123L, 456L);
        verify(unknownBotCommand, never()).execute(anyLong(), anyLong(), anyString());
    }

    @Test
    void handleHelpCommand() {
        update.getMessage().setText("/help");
        when(helpBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(helpBotCommand).execute(123L, 456L);
    }

    @Test
    void handleTodayScheduleCommand() {
        update.getMessage().setText("/today");
        when(todayScheduleBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(todayScheduleBotCommand).execute(123L, 456L);
    }

    @Test
    void handleTomorrowScheduleCommand() {
        update.getMessage().setText("/tomorrow");
        when(tomorrowScheduleBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(tomorrowScheduleBotCommand).execute(123L, 456L);
    }

    @Test
    void handleWeekScheduleCommand() {
        update.getMessage().setText("/week");
        when(weekScheduleBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(weekScheduleBotCommand).execute(123L, 456L);
    }

    @Test
    void handleRegisterCommand() {
        update.getMessage().setText("/register");
        when(registerBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(registerBotCommand).execute(123L, 456L);
    }

    @Test
    void handleInfoCommand() {
        update.getMessage().setText("/info");
        when(infoBotCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(infoBotCommand).execute(123L, 456L);
    }

    @Test
    void handleTomorrowScheduleNotificationSettings() {
        update.getMessage().setText("/tomorrow_notification");
        when(tomorrowScheduleNotificationSettingsCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(tomorrowScheduleNotificationSettingsCommand).execute(123L, 456L);
    }

    @Test
    void handleDifferenceScheduleNotificationSettings() {
        update.getMessage().setText("/difference_notification");
        when(differenceScheduleNotificationSettingsCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(differenceScheduleNotificationSettingsCommand).execute(123L, 456L);
    }

    @Test
    void handleShowRegisteredStudents() {
        update.getMessage().setText("/show_registered_users");
        when(showRegisteredStudentsCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(showRegisteredStudentsCommand).execute(123L, 456L);
    }

    @Test
    void handleAdminCommands() {
        update.getMessage().setText("/admin");
        when(adminCommand.execute(123L, 456L)).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
        verify(adminCommand).execute(123L, 456L);
    }

    @Test
    void handleSendMessagesToUsers() {
        update.getMessage().setText("/send_message_to_users argument");
        when(sendMessageToUsersCommand.execute(123L, 456L, "argument")).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
    }

    // Тест для обработки неизвестной команды.
    @Test
    void handleUnknownCommands() {
        update.getMessage().setText("/startasdgadsgsdfh");
        when(unknownBotCommand.execute(anyLong(), anyLong(), anyString())).thenReturn(expectedMessage);

        SendMessage result = commandHandler.handleCommands(update);

        assertEquals(expectedMessage, result);
    }
}