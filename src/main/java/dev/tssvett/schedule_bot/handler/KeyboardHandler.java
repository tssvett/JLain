package dev.tssvett.schedule_bot.handler;

import dev.tssvett.schedule_bot.constants.ReplyMessages;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class KeyboardHandler {

    public SendMessage handleKeyboardAction(Update update) {
        String messageText = update.getMessage().getText();
        String command = messageText.split(" ")[0];
        long chatId = update.getMessage().getChatId();
        switch (command) {
            case "/start":
                return new SendMessage(String.valueOf(chatId), ReplyMessages.START_COMMAND);
            case "/help":
                return new SendMessage(String.valueOf(chatId), ReplyMessages.HELP_COMMAND);
            default:
                return new SendMessage(String.valueOf(chatId), ReplyMessages.UNAVAILABLE_COMMAND);
        }
    }
}
