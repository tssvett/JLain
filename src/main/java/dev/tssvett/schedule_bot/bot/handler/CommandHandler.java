package dev.tssvett.schedule_bot.bot.handler;

import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.enums.constants.CommandNames;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final BotCommand startBotCommand;
    private final BotCommand helpBotCommand;
    private final BotCommand todayScheduleBotCommand;
    private final BotCommand tomorrowScheduleBotCommand;
    private final BotCommand weekScheduleBotCommand;
    private final BotCommand registerBotCommand;
    private final BotCommand unknownBotCommand;
    private final BotCommand infoBotCommand;
    private final BotCommand tomorrowScheduleNotificationSettingsCommand;
    private final BotCommand differenceScheduleNotificationSettingsCommand;
    private final BotCommand showRegisteredStudentsCommand;
    private final BotCommand adminCommand;
    private final BotCommand sendMessageToUsersCommand;
    private static final String EMPTY_ARGUMENT = "";

    public SendMessage handleCommands(Update update) {
        Long userId = UpdateUtils.getUserIdFromMessage(update);
        Long chatId = UpdateUtils.getChatIdFromMessage(update);

        Optional<CommandNames> optionalCommandName = CommandNames.fromCommandName(
                UpdateUtils.getFirstWordFromMessage(update)
        );

        Optional<String> optionalArguments = UpdateUtils.getArgumentsFromMessage(update);

        if (optionalCommandName.isEmpty()) {
            return unknownBotCommand.execute(userId, chatId, EMPTY_ARGUMENT);
        }

        CommandNames commandName = optionalCommandName.get();
        String argument = getArgument(optionalArguments);
        log.info("Received [{}] with argument [{}] from user [{}]", commandName.getCommandName(), argument, userId);

        return switch (commandName) {
            case START_COMMAND -> startBotCommand.execute(userId, chatId);
            case HELP_COMMAND -> helpBotCommand.execute(userId, chatId);
            case TODAY_COMMAND -> todayScheduleBotCommand.execute(userId, chatId);
            case TOMORROW_COMMAND -> tomorrowScheduleBotCommand.execute(userId, chatId);
            case WEEK_COMMAND -> weekScheduleBotCommand.execute(userId, chatId);
            case REGISTER_COMMAND -> registerBotCommand.execute(userId, chatId);
            case INFO_COMMAND -> infoBotCommand.execute(userId, chatId);
            case TOMORROW_SCHEDULE_NOTIFICATION_COMMAND ->
                    tomorrowScheduleNotificationSettingsCommand.execute(userId, chatId);
            case DIFFERENCE_SCHEDULE_NOTIFICATION_COMMAND ->
                    differenceScheduleNotificationSettingsCommand.execute(userId, chatId);
            case SHOW_REGISTERED_USERS_COMMAND -> showRegisteredStudentsCommand.execute(userId, chatId);
            case ADMIN_COMMAND -> adminCommand.execute(userId, chatId);
            case SEND_MESSAGE_TO_USERS_COMMAND -> sendMessageToUsersCommand.execute(userId, chatId, argument);
        };
    }


    private String getArgument(Optional<String> optionalArguments) {
        String argument = EMPTY_ARGUMENT;
        if (optionalArguments.isPresent()) {
            argument = optionalArguments.get();
        }
        return argument;
    }
}