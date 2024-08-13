package dev.tssvett.schedule_bot.actions.handler;

import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.impl.CourseKeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.impl.FacultyKeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.impl.GroupKeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.impl.ReRegistrateCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyboardHandler {
    private final FacultyKeyboardCallback facultyKeyboardCallback;
    private final CourseKeyboardCallback courseKeyboardCallback;
    private final GroupKeyboardCallback groupKeyboardCallback;
    private final ReRegistrateCallback reRegistrateCallback;


    public SendMessage handleKeyboardAction(Update update) {
        return switch (CallbackDetails.fromString(update.getCallbackQuery().getData()).getAction()) {
            case FACULTY_CHOOSE -> facultyKeyboardCallback.callback(update);
            case COURSE_CHOOSE -> courseKeyboardCallback.callback(update);
            case GROUP_CHOOSE -> groupKeyboardCallback.callback(update);
            case REREGISTRATE -> reRegistrateCallback.callback(update);
        };
    }
}
