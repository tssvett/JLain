package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.actions.keyboard.impl.FacultyKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import dev.tssvett.schedule_bot.schedule.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;

@Slf4j
@Component
public class RegisterCommand implements Command {

    @Override
    public SendMessage execute(Update update) {
        FacultyParser facultyParser = new FacultyParser();
        FacultyKeyboard facultyKeyboard = new FacultyKeyboard(facultyParser);
        String chatId = String.valueOf(update.getMessage().getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE));
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_FACULTY_MESSAGE);
        return sendMessage;
    }
}
