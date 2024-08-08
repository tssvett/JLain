package dev.tssvett.schedule_bot.actions.keyboard.callback;


import dev.tssvett.schedule_bot.actions.command.schedule.group.Group;
import dev.tssvett.schedule_bot.actions.command.schedule.parser.GroupParser;
import dev.tssvett.schedule_bot.actions.keyboard.inline.InlineKeyboardMaker;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboardCallback {
    private final GroupParser groupParser;
    private final InlineKeyboardMaker inlineKeyboardMaker;

    public SendMessage callback(String chatId, CallbackDetails callbackDetails) {
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

