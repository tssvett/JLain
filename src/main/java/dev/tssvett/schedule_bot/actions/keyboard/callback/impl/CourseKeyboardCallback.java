package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.impl.GroupKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardCallback implements KeyboardCallback {

    private final GroupKeyboard groupKeyboard;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Integer courseNumber = Integer.parseInt(callbackDetails.getCallbackText());
        //db.save(userId, courseNumber, currentRegistrationState)
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE);
        sendMessage.setReplyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE));
        return sendMessage;
    }

}
