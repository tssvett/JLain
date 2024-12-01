package dev.tssvett.schedule_bot.bot.keyboard.impl.admin;

import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.enums.constants.CommandNames;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class AdminCommandSelectionKeyboardButton implements KeyboardButton {
    private final BotCommand showRegisteredStudentsCommand;
    private final BotCommand helpSendMessageCommand;

    @Override
    public SendMessage onButtonClick(Update update) {
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        String command = UpdateUtils.getAdminCommand(update);

        return processAdminCommandSelectionOnButtonClick(userId, chatId, command);
    }

    private SendMessage processAdminCommandSelectionOnButtonClick(long userId, long chatId, String command) {
        CommandNames commandName = CommandNames.fromCommandName(command)
                .orElseThrow(() -> new IllegalArgumentException("Unknown command: " + command));

        return switch (commandName) {
            case SHOW_REGISTERED_USERS_COMMAND -> showRegisteredStudentsCommand.execute(userId, chatId);
            case SEND_MESSAGE_TO_USERS_COMMAND -> helpSendMessageCommand.execute(userId, chatId);
            default -> null;
        };
    }
}
