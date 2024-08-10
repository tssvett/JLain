package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;


import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.schedule.group.Group;
import dev.tssvett.schedule_bot.schedule.parser.GroupParser;
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
public class GroupKeyboardCallback implements KeyboardCallback {
    private final GroupParser groupParser;
    private final RegistrationService registrationService;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Group group = findGroupById(callbackDetails.getCallbackText());

        return registrationService.chooseGroupStepCallback(userId, chatId, group);
    }

    /*
    Это нужно будет переписать когда появится таблица с расписаниями для групп
    Все факультеты нужно будет парсить раз в n часов(минут?) и сохранять в бд
    Уже из бд брать все факультеты и класить в список
    Из списка по id находить нужный нам элемент
     */
    private Group findGroupById(String id) {
        List<Group> groupList = groupParser.parse();
        for (Group group : groupList) {
            if (id.equals(group.getId())) {
                return group;
            }
        }
        return null;
    }
}

