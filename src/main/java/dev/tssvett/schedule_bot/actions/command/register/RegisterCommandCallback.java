package dev.tssvett.schedule_bot.actions.command.register;

import dev.tssvett.schedule_bot.actions.keyboard.inline.InlineKeyboardMaker;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;

@Component
@RequiredArgsConstructor
public class RegisterCommandCallback {

    private final InlineKeyboardMaker inlineKeyboardMaker;

    public SendMessage sendStartFacultyChooseMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMaker.createFacultyKeyboard(FACULTY_CHOOSE));
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_FACULTY_MESSAGE);
        return sendMessage;
    }
}
