package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.course.CourseKeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.faculty.FacultyKeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.group.GroupKeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.notification.NotificationKeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.reregister.ReRegistrateButton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyboardHandler {
    private final FacultyKeyboardButton facultyKeyboardCallback;
    private final CourseKeyboardButton courseKeyboardCallback;
    private final GroupKeyboardButton groupKeyboardCallback;
    private final ReRegistrateButton reRegistrateCallback;
    private final NotificationKeyboardButton notificationKeyboardCallback;


    public SendMessage handleKeyboardAction(Update update) {
        return switch (CallbackDetails.fromString(update.getCallbackQuery().getData()).getAction()) {
            case FACULTY_CHOOSE -> facultyKeyboardCallback.click(update);
            case COURSE_CHOOSE -> courseKeyboardCallback.click(update);
            case GROUP_CHOOSE -> groupKeyboardCallback.click(update);
            case REREGISTRATE -> reRegistrateCallback.click(update);
            case NOTIFICATION -> notificationKeyboardCallback.click(update);
        };
    }
}
