package dev.tssvett.schedule_bot.actions.keyboard.callback;

import dev.tssvett.schedule_bot.actions.command.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.actions.command.schedule.parser.FacultyParser;
import dev.tssvett.schedule_bot.actions.keyboard.inline.InlineKeyboardMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static dev.tssvett.schedule_bot.enums.Action.COURSE_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardCallback {
    private final FacultyParser facultyParser;
    private final InlineKeyboardMaker inlineKeyboardMaker;

    public SendMessage callback(String chatId, CallbackDetails callbackDetails) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Faculty faculty = findFacultyById(callbackDetails.getCallbackText());
        //db.save(userId, faculty, currentRegistrationState)
        sendMessage.setText("Отличный выбор! Теперь выберем курс!");
        sendMessage.setReplyMarkup(inlineKeyboardMaker.createCourseKeyboard(COURSE_CHOOSE));
        return sendMessage;
    }

    private Faculty findFacultyById(String id){
        List<Faculty> facultyList = facultyParser.parse();
        for (Faculty faculty : facultyList) {
            if (id.equals(faculty.getId())) {
                return faculty;
            }
        }
        return null;
    }
}
