package dev.tssvett.schedule_bot.bot.command.impl.settings;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule.ScheduleDifferenceNotificationKeyboard;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.SETUP_NOTIFICATION;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@ExtendWith(MockitoExtension.class)
class DifferenceScheduleNotificationSettingsCommandTest {
    @Mock
    ScheduleDifferenceNotificationKeyboard scheduleDifferenceNotificationKeyboard;

    @InjectMocks
    DifferenceScheduleNotificationSettingsCommand differenceScheduleNotificationSettingsCommand;

    @Test
    void execute_happyPath() {
        //Arrange
        when(scheduleDifferenceNotificationKeyboard.createInlineKeyboard(Action.SCHEDULE_DIFFERENCE_NOTIFICATION,
                1L)).thenReturn(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow())));

        //Act
        SendMessage result = differenceScheduleNotificationSettingsCommand.execute(1L, 1L);

        //Assert
        verify(scheduleDifferenceNotificationKeyboard).createInlineKeyboard(any(), any());
        assertEquals(SETUP_NOTIFICATION, result.getText());
    }
}