package dev.tssvett.schedule_bot.actions.command.handler;

import dev.tssvett.schedule_bot.actions.command.MessageMaker;
import dev.tssvett.schedule_bot.actions.command.register.RegisterCommandCallback;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.enums.Command;
import dev.tssvett.schedule_bot.actions.keyboard.inline.InlineKeyboardMaker;
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
import static dev.tssvett.schedule_bot.constants.CommandConstants.START;
import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final MessageMaker messageMaker;
    private final RegisterCommandCallback registerCommandCallback;
    public SendMessage handleCommands(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        String chatId = String.valueOf(update.getMessage().getChatId());
        log.info("Command: " + command);
        if (!messageIsAvailableCommand(command)) {
            return new SendMessage(String.valueOf(update.getMessage().getChatId()), MessageConstants.UNAVAILABLE_COMMAND);
        } else {
            return switch (command) {
                case START ->
                        messageMaker.createMessage(chatId, MessageConstants.START_COMMAND, inlineKeyboardMaker.createInlineKeyboard());
                case HELP ->
                        messageMaker.createMessage(chatId, MessageConstants.HELP_COMMAND);
                //case SCHEDULE ->
                        //messageMaker.createScheduleMessage(chatId, MessageConstants.SCHEDULE_COMMAND,
                                //inlineKeyboardMaker.createFacultyKeyboard(SCHEDULE));
                case PICTURE ->
                        messageMaker.createMessage(chatId, MessageConstants.PICTURE_COMMAND);
                case REGISTER ->
                    registerCommandCallback.sendStartFacultyChooseMessage(chatId);

                case GADIT ->
                        messageMaker.createMessage("-4191336905", "почините меня :x");
                default ->
                        new SendMessage(chatId, MessageConstants.UNAVAILABLE_COMMAND);
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
