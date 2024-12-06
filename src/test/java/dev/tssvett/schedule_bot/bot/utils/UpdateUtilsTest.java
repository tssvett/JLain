package dev.tssvett.schedule_bot.bot.utils;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

class UpdateUtilsTest {
    Update update;

    @BeforeEach
    void setUp() {
        update = new Update();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message = new Message();
        Chat chat = new Chat(123L, "type");
        User user = new User(123L, "username", false);
        message.setChat(chat);
        message.setText("test");
        message.setFrom(user);
        callbackQuery.setMessage(message);
        callbackQuery.setFrom(user);
        update.setCallbackQuery(callbackQuery);
        update.setMessage(message);
    }

    @Test
    void getChatIdFromCallbackQuery() {
        Long expectedId = 123L;

        Long actualId = UpdateUtils.getChatIdFromCallbackQuery(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getChatIdFromMessage() {
        Long expectedId = 123L;

        Long actualId = UpdateUtils.getChatIdFromMessage(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getUserIdFromCallbackQuery() {
        Long expectedId = 123L;

        Long actualId = UpdateUtils.getUserIdFromCallbackQuery(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getUserIdFromMessage() {
        Long expectedId = 123L;

        Long actualId = UpdateUtils.getUserIdFromMessage(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getFirstWordFromMessage() {
        String expectedWord = "test";

        String actualWord = UpdateUtils.getFirstWordFromMessage(update);

        assertEquals(expectedWord, actualWord);
    }

    @Test
    void getFacultyId() {
        update.getCallbackQuery().setData("FACULTY_CHOOSE 123");

        Long expectedId = 123L;

        Long actualId = UpdateUtils.getFacultyId(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getGroupId() {

        update.getCallbackQuery().setData("GROUP_CHOOSE 123");

        Long expectedId = 123L;

        Long actualId = UpdateUtils.getGroupId(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getCourse() {
        update.getCallbackQuery().setData("COURSE_CHOOSE 123");

        Long expectedId = 123L;

        Long actualId = UpdateUtils.getCourse(update);

        assertEquals(expectedId, actualId);
    }

    @Test
    void getTomorrowScheduleNotificationStatus() {
        update.getCallbackQuery().setData("TOMORROW_SCHEDULE_NOTIFICATION Включить");

        boolean expectedStatus = true;

        boolean actualStatus = UpdateUtils.getTomorrowScheduleNotificationStatus(update);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getRefreshRegistrationStatus() {
        update.getCallbackQuery().setData("REFRESH_REGISTRATION Да");

        boolean expectedStatus = true;

        boolean actualStatus = UpdateUtils.getRefreshRegistrationStatus(update);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void messageIsText() {
        boolean expectedStatus = true;

        boolean actualStatus = UpdateUtils.messageIsText(update);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getAdminCommand() {
        update.getCallbackQuery().setData("ADMIN_COMMAND_SELECTION admin");

        String expectedCommand = "admin";

        String actualCommand = UpdateUtils.getAdminCommand(update);

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    void getArgumentsFromMessage_argsExists_returnArgs() {
        update.getMessage().setText("command arg1 arg2");
        Optional<String> expectedArguments = Optional.of("arg1 arg2");

        Optional<String> actualArguments = UpdateUtils.getArgumentsFromMessage(update);

        assertEquals(expectedArguments, actualArguments);
    }

    @Test
    void getArgumentsFromMessage_argsNotExists_returnNoArgs() {
        update.getMessage().setText("command");
        Optional<String> expectedArguments = Optional.empty();

        Optional<String> actualArguments = UpdateUtils.getArgumentsFromMessage(update);

        assertEquals(expectedArguments, actualArguments);
    }
}