package dev.tssvett.schedule_bot.actions.handler;

import dev.tssvett.schedule_bot.actions.command.impl.HelpCommand;
import dev.tssvett.schedule_bot.actions.command.impl.PictureCommand;
import dev.tssvett.schedule_bot.actions.command.impl.RegisterCommand;
import dev.tssvett.schedule_bot.actions.command.impl.ScheduleCommand;
import dev.tssvett.schedule_bot.actions.command.impl.StartCommand;
import dev.tssvett.schedule_bot.actions.command.impl.ToChatCommand;
import dev.tssvett.schedule_bot.actions.command.impl.UnknownCommand;
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
@RequiredArgsConstructor
public class CommandHandler {
    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final ScheduleCommand scheduleCommand;
    private final PictureCommand pictureCommand;
    private final RegisterCommand registerCommand;
    private final ToChatCommand toChatCommand;
    private final UnknownCommand unknownCommand;


    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
        } else {
            return switch (getBaseCommand(command)) {
                case START -> startCommand.execute(userId, chatId);
                case HELP -> helpCommand.execute(userId, chatId);
                case SCHEDULE -> scheduleCommand.execute(userId, chatId);
                case PICTURE -> pictureCommand.execute(userId, chatId);
                case REGISTER -> registerCommand.execute(userId, chatId);
                case GADIT -> toChatCommand.execute(userId, chatId);
                default -> unknownCommand.execute(userId, chatId);
            };
        }
    }

    private boolean messageIsAvailableCommand(String command) {
        return command.startsWith("/") && isCommandInEnum(command);
    }

    private boolean isCommandInEnum(String command) {
        return Arrays.stream(Command.values())
                .anyMatch(enumCommand -> {
                    String commandName = enumCommand.getCommandName();
                    return command.equals(commandName) ||
                            command.equalsIgnoreCase(commandName + "@JLainBot");
                });
    }

    private String getBaseCommand(String command) {
        int atIndex = command.indexOf("@");
        if (atIndex != -1) {
            return command.substring(0, atIndex);
        } else {
            return command;
        }
    }
}
