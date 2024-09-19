package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.enums.Command;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.HELP;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.INFO;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.NOTIFICATION;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.PICTURE;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.REGISTER;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.START;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.TODAY;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.TOMORROW;
import static dev.tssvett.schedule_bot.bot.constants.CommandConstants.WEEK;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final BotCommand startBotCommand;
    private final BotCommand helpBotCommand;
    private final BotCommand todayScheduleBotCommand;
    private final BotCommand tomorrowScheduleBotCommand;
    private final BotCommand weekScheduleBotCommand;
    private final BotCommand pictureBotCommand;
    private final BotCommand registerBotCommand;
    private final BotCommand unknownBotCommand;
    private final BotCommand infoBotCommand;
    private final BotCommand notificationBotCommand;

    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
        } else {
            return switch (getBaseCommand(command)) {
                case START -> startBotCommand.execute(userId, chatId);
                case HELP -> helpBotCommand.execute(userId, chatId);
                case TODAY -> todayScheduleBotCommand.execute(userId, chatId);
                case TOMORROW -> tomorrowScheduleBotCommand.execute(userId, chatId);
                case WEEK -> weekScheduleBotCommand.execute(userId, chatId);
                case PICTURE -> pictureBotCommand.execute(userId, chatId);
                case REGISTER -> registerBotCommand.execute(userId, chatId);
                case INFO -> infoBotCommand.execute(userId, chatId);
                case NOTIFICATION -> notificationBotCommand.execute(userId, chatId);
                default -> unknownBotCommand.execute(userId, chatId);
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