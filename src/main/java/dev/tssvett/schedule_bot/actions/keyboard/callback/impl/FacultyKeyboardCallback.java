package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardCallback implements KeyboardCallback {
    private final FacultyParser facultyParser;
    private final RegistrationService registrationService;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Faculty faculty = findFacultyById(callbackDetails.getCallbackText());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return registrationService.chooseFacultyStepCallback(userId, chatId, faculty);
    }


    /*
    Это нужно будет переписать когда появится таблица с расписаниями для факультетов
    Все факультеты нужно будет парсить раз в n часов(минут?) и сохранять в бд
    Уже из бд брать все факультеты и класить в список
    Из списка по id находить нужный нам элемент
     */
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
