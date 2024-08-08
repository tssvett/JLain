package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.impl.CourseKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static dev.tssvett.schedule_bot.enums.Action.COURSE_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardCallback implements KeyboardCallback {
    private final FacultyParser facultyParser;
    private final CourseKeyboard courseKeyboard;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Faculty faculty = findFacultyById(callbackDetails.getCallbackText());
        //db.save(userId, faculty, currentRegistrationState)
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_COURSE_MESSAGE);
        sendMessage.setReplyMarkup(courseKeyboard.createInlineKeyboard(COURSE_CHOOSE));
        return sendMessage;
    }

    private Faculty findFacultyById(String id) {
        List<Faculty> facultyList = facultyParser.parse();
        for (Faculty faculty : facultyList) {
            if (id.equals(faculty.getId())) {
                return faculty;
            }
        }
        return null;
    }
}
