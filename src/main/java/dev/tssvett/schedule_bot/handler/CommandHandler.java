package dev.tssvett.schedule_bot.handler;

import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.enums.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static dev.tssvett.schedule_bot.constants.CommandConstants.HELP;
import static dev.tssvett.schedule_bot.constants.CommandConstants.PICTURE;
import static dev.tssvett.schedule_bot.constants.CommandConstants.REGISTER;
import static dev.tssvett.schedule_bot.constants.CommandConstants.SCHEDULE;
import static dev.tssvett.schedule_bot.constants.CommandConstants.START;

@Component
@Slf4j
public class CommandHandler {

    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        log.info("Command: " + command);
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
        } else {
            return switch (command) {
                case START ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.START_COMMAND);
                case HELP ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.HELP_COMMAND);
                case SCHEDULE ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.SCHEDULE_COMMAND);
                case PICTURE ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.PICTURE_COMMAND);
                case REGISTER ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.REGISTER_COMMAND);
                default ->
                        new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
            };
        }
    }

    private boolean messageIsAvailableCommand(String command) {
        return command.startsWith("/") && isCommandInEnum(command);
    }

    private boolean isCommandInEnum(String command) {
        return Arrays.stream(Command.values()).anyMatch(enumCommand -> enumCommand.getCommandName().equals(command));
    }
}
