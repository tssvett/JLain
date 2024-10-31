package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.enums.constants.CommandNames;
import dev.tssvett.schedule_bot.bot.utils.CommandUtils;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;


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

    public BotApiMethod<?> handleCommands(Update update) {
        Long userId = UpdateUtils.getUserIdFromMessage(update);
        Long chatId = UpdateUtils.getChatIdFromMessage(update);

        Optional<CommandNames> optionalCommandName =  CommandNames.fromCommandName(
                UpdateUtils.getFirstWordFromMessage(update)
        );

        if (optionalCommandName.isEmpty()) {
            return unknownBotCommand.execute(userId, chatId);
        }

        CommandNames commandName = optionalCommandName.get();
        log.info("Received [{}] from user [{}]", commandName.getCommandName(), userId);

        return switch (commandName) {
            case START_COMMAND -> startBotCommand.execute(userId, chatId);
            case HELP_COMMAND -> helpBotCommand.execute(userId, chatId);
            case TODAY_COMMAND -> todayScheduleBotCommand.execute(userId, chatId);
            case TOMORROW_COMMAND -> tomorrowScheduleBotCommand.execute(userId, chatId);
            case WEEK_COMMAND -> weekScheduleBotCommand.execute(userId, chatId);
            case PICTURE_COMMAND -> pictureBotCommand.execute(userId, chatId);
            case REGISTER_COMMAND -> registerBotCommand.execute(userId, chatId);
            case INFO_COMMAND -> infoBotCommand.execute(userId, chatId);
            case NOTIFICATION_COMMAND -> notificationBotCommand.execute(userId, chatId);
        };
    }
}