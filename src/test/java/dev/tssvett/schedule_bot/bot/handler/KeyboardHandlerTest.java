package dev.tssvett.schedule_bot.bot.handler;

import dev.tssvett.schedule_bot.bot.keyboard.impl.admin.AdminCommandSelectionKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.course.CourseSelectionKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultySelectionKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.group.GroupSelectionKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule.ScheduleDifferenceNotificationKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.tomorrowschedule.TomorrowScheduleNotificationKeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.refresh.RefreshRegistrationSelectionKeyboardButton;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class KeyboardHandlerTest {
    private KeyboardHandler keyboardHandler;

    @Mock
    private FacultySelectionKeyboardButton facultySelectionKeyboardButton;

    @Mock
    private CourseSelectionKeyboardButton courseSelectionKeyboardButton;

    @Mock
    private GroupSelectionKeyboardButton groupSelectionKeyboardButton;

    @Mock
    private RefreshRegistrationSelectionKeyboardButton refreshRegistrationSelectionKeyboardButton;

    @Mock
    private TomorrowScheduleNotificationKeyboardButton tomorrowScheduleNotificationKeyboardButton;

    @Mock
    private ScheduleDifferenceNotificationKeyboardButton scheduleDifferenceNotificationKeyboardButton;

    @Mock
    private AdminCommandSelectionKeyboardButton adminCommandSelectionKeyboardButton;

    private Update update;
    private SendMessage expectedMessage;

    @BeforeEach
    void setUp() {
        update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        expectedMessage = new SendMessage("12345", "text");
        keyboardHandler = new KeyboardHandler(
                facultySelectionKeyboardButton,
                courseSelectionKeyboardButton,
                groupSelectionKeyboardButton,
                refreshRegistrationSelectionKeyboardButton,
                tomorrowScheduleNotificationKeyboardButton,
                scheduleDifferenceNotificationKeyboardButton,
                adminCommandSelectionKeyboardButton
        );
    }

    @Test
    void testHandleFacultyChoose() {
        update.getCallbackQuery().setData("FACULTY_CHOOSE 1235");

        when(facultySelectionKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(facultySelectionKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleCourseChoose() {
        update.getCallbackQuery().setData("COURSE_CHOOSE 3");

        when(courseSelectionKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(courseSelectionKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleGroupChoose() {
        update.getCallbackQuery().setData("GROUP_CHOOSE 124125");

        when(groupSelectionKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(groupSelectionKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleRefreshRegistration() {
        update.getCallbackQuery().setData("REFRESH_REGISTRATION Да");

        when(refreshRegistrationSelectionKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(refreshRegistrationSelectionKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleTomorrowScheduleNotification() {
        update.getCallbackQuery().setData("TOMORROW_SCHEDULE_NOTIFICATION Включить");

        when(tomorrowScheduleNotificationKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(tomorrowScheduleNotificationKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleScheduleDifferenceNotification() {
        update.getCallbackQuery().setData("SCHEDULE_DIFFERENCE_NOTIFICATION Включить");

        when(scheduleDifferenceNotificationKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(scheduleDifferenceNotificationKeyboardButton).onButtonClick(update);
    }

    @Test
    void testHandleAdminCommandSelection() {
        update.getCallbackQuery().setData("ADMIN_COMMAND_SELECTION admin");

        when(adminCommandSelectionKeyboardButton.onButtonClick(update)).thenReturn(expectedMessage);

        SendMessage result = keyboardHandler.handleKeyboardAction(update);

        assertEquals(expectedMessage, result);
        verify(adminCommandSelectionKeyboardButton).onButtonClick(update);
    }
}