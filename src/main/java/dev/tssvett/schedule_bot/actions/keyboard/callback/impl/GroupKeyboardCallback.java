package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;


import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.entity.Group;
import dev.tssvett.schedule_bot.repository.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final RegistrationService registrationService;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Group group = findGroupById(Long.parseLong(callbackDetails.getCallbackText()));

        return registrationService.chooseGroupStepCallback(userId, chatId, group);
    }

    private Group findGroupById(Long id) {
        List<Group> groupList = groupRepository.findAll();
        for (Group group : groupList) {
            if (id.equals(group.getGroupId())) {
                return group;
            }
        }
        return null;
    }

}
