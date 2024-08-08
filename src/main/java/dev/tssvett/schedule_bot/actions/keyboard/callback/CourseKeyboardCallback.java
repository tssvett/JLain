package dev.tssvett.schedule_bot.actions.keyboard.callback;

import dev.tssvett.schedule_bot.actions.keyboard.inline.InlineKeyboardMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardCallback {
    private final InlineKeyboardMaker inlineKeyboardMaker;

    public SendMessage callback(String chatId, CallbackDetails callbackDetails) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Integer courseNumber = Integer.parseInt(callbackDetails.getCallbackText());
        //db.save(userId, courseNumber, currentRegistrationState)
        sendMessage.setText("Курс выбран. Теперь выберем вашу группу!");
        sendMessage.setReplyMarkup(inlineKeyboardMaker.createGroupKeyboard(GROUP_CHOOSE));
        return sendMessage;
    }

}
