package dev.tssvett.schedule_bot.handler;

import dev.tssvett.schedule_bot.constants.ReplyMessages;
import dev.tssvett.schedule_bot.enums.Commands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Component
@Slf4j
public class CommandHandler {

    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        log.info("Command: " + command);
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), ReplyMessages.UNAVAILABLE_COMMAND);
        } else {
            return switch (command) {
                case "/start" ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), ReplyMessages.START_COMMAND);
                case "/help" ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), ReplyMessages.HELP_COMMAND);
                default ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), ReplyMessages.UNAVAILABLE_COMMAND);
            };
        }
    }

    private boolean messageIsAvailableCommand(String command) {
        return command.startsWith("/") && isCommandInEnum(command);
    }

    private boolean isCommandInEnum(String command) {
        return Arrays.stream(Commands.values()).anyMatch(enumCommand -> enumCommand.getCommand().equals(command));
    }
}
