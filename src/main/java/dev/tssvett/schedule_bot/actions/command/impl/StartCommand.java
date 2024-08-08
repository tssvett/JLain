package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class StartCommand implements Command {

    @Override
    public SendMessage execute(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        String chatId = String.valueOf(update.getMessage().getChatId());
        log.info("Command: " + command);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(MessageConstants.START_COMMAND);
        return sendMessage;
    }
}
