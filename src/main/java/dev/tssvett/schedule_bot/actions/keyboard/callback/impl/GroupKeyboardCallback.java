package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;


import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.schedule.group.Group;
import dev.tssvett.schedule_bot.schedule.parser.GroupParser;
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

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Group group = findGroupById(callbackDetails.getCallbackText());
        //db.save(userId, group, callbackDetails.getAction())
        sendMessage.setText(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE);
        log.info(sendMessage.getText());
        return sendMessage;
    }

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

