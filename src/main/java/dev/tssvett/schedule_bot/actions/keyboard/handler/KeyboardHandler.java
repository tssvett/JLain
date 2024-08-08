package dev.tssvett.schedule_bot.actions.keyboard.handler;

import dev.tssvett.schedule_bot.actions.keyboard.callback.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.CourseKeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.FacultyKeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.GroupKeyboardCallback;
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


    public SendMessage handleKeyboardAction(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        return switch (callbackDetails.getAction()) {
            case FACULTY_CHOOSE -> facultyKeyboardCallback.callback(chatId, callbackDetails);
            case COURSE_CHOOSE -> courseKeyboardCallback.callback(chatId, callbackDetails);
            case GROUP_CHOOSE -> groupKeyboardCallback.callback(chatId, callbackDetails);
        };
    }
}
