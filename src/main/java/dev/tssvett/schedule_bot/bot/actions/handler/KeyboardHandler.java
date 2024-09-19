package dev.tssvett.schedule_bot.bot.actions.handler;

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
    private final KeyboardButton facultyKeyboardButton;
    private final KeyboardButton courseKeyboardButton;
    private final KeyboardButton groupKeyboardButton;
    private final KeyboardButton reRegistrateButton;
    private final KeyboardButton notificationKeyboardButton;

    public SendMessage handleKeyboardAction(Update update) {
        return switch (CallbackDetails.fromString(update.getCallbackQuery().getData()).getAction()) {
            case FACULTY_CHOOSE -> facultyKeyboardButton.click(update);
            case COURSE_CHOOSE -> courseKeyboardButton.click(update);
            case GROUP_CHOOSE -> groupKeyboardButton.click(update);
            case REREGISTRATE -> reRegistrateButton.click(update);
            case NOTIFICATION -> notificationKeyboardButton.click(update);
        };
    }
}
