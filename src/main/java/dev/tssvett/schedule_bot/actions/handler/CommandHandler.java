package dev.tssvett.schedule_bot.actions.handler;

import dev.tssvett.schedule_bot.actions.command.impl.HelpCommand;
import dev.tssvett.schedule_bot.actions.command.impl.PictureCommand;
import dev.tssvett.schedule_bot.actions.command.impl.RegisterCommand;
import dev.tssvett.schedule_bot.actions.command.impl.ScheduleCommand;
import dev.tssvett.schedule_bot.actions.command.impl.StartCommand;
import dev.tssvett.schedule_bot.actions.command.impl.ToChatCommand;
import dev.tssvett.schedule_bot.actions.command.impl.UnknownCommand;
import dev.tssvett.schedule_bot.actions.keyboard.impl.FacultyKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.enums.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static dev.tssvett.schedule_bot.constants.CommandConstants.GADIT;
import static dev.tssvett.schedule_bot.constants.CommandConstants.HELP;
import static dev.tssvett.schedule_bot.constants.CommandConstants.PICTURE;
import static dev.tssvett.schedule_bot.constants.CommandConstants.REGISTER;
import static dev.tssvett.schedule_bot.constants.CommandConstants.SCHEDULE;
import static dev.tssvett.schedule_bot.constants.CommandConstants.START;

@Slf4j
@Component
public class CommandHandler {
    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
        } else {
            return switch (command) {
                case START -> new StartCommand().execute(update);
                case HELP -> new HelpCommand().execute(update);
                case SCHEDULE -> new ScheduleCommand().execute(update);
                case PICTURE -> new PictureCommand().execute(update);
                case REGISTER -> new RegisterCommand().execute(update);
                case GADIT -> new ToChatCommand().execute(update);
                default -> new UnknownCommand().execute(update);
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
