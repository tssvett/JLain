package dev.tssvett.schedule_bot.bot.handler;

import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyboardHandler {
    private final KeyboardButton facultySelectionKeyboardButton;
    private final KeyboardButton courseSelectionKeyboardButton;
    private final KeyboardButton groupSelectionKeyboardButton;
    private final KeyboardButton refreshRegistrationSelectionKeyboardButton;
    private final KeyboardButton tomorrowScheduleNotificationKeyboardButton;
    private final KeyboardButton scheduleDifferenceNotificationKeyboardButton;
    private final KeyboardButton adminCommandSelectionKeyboardButton;

    public SendMessage handleKeyboardAction(Update update) {
        return switch (CallbackDetails.fromString(update.getCallbackQuery().getData()).getAction()) {
            case FACULTY_CHOOSE -> facultySelectionKeyboardButton.onButtonClick(update);
            case COURSE_CHOOSE -> courseSelectionKeyboardButton.onButtonClick(update);
            case GROUP_CHOOSE -> groupSelectionKeyboardButton.onButtonClick(update);
            case REFRESH_REGISTRATION -> refreshRegistrationSelectionKeyboardButton.onButtonClick(update);
            case TOMORROW_SCHEDULE_NOTIFICATION -> tomorrowScheduleNotificationKeyboardButton.onButtonClick(update);
            case SCHEDULE_DIFFERENCE_NOTIFICATION -> scheduleDifferenceNotificationKeyboardButton.onButtonClick(update);
            case ADMIN_COMMAND_SELECTION -> adminCommandSelectionKeyboardButton.onButtonClick(update);
        };
    }
}
