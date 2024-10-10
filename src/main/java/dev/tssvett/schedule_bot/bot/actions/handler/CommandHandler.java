package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.utils.CommandUtils;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.HELP_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.INFO_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.NOTIFICATION_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.PICTURE_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.REGISTER_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.START_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TODAY_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TOMORROW_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.WEEK_COMMAND;


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
        Long userId = UpdateUtils.getUserIdFromMessage(update);
        Long chatId = UpdateUtils.getChatIdFromMessage(update);

        String command = CommandUtils.parseCommandFromMessage(UpdateUtils.getFirstWordFromMessage(update));
        log.info("Received [{}] from user [{}]", command, userId);

        return switch (command) {
            case START_COMMAND -> startBotCommand.execute(userId, chatId);
            case HELP_COMMAND -> helpBotCommand.execute(userId, chatId);
            case TODAY_COMMAND -> todayScheduleBotCommand.execute(userId, chatId);
            case TOMORROW_COMMAND -> tomorrowScheduleBotCommand.execute(userId, chatId);
            case WEEK_COMMAND -> weekScheduleBotCommand.execute(userId, chatId);
            case PICTURE_COMMAND -> pictureBotCommand.execute(userId, chatId);
            case REGISTER_COMMAND -> registerBotCommand.execute(userId, chatId);
            case INFO_COMMAND -> infoBotCommand.execute(userId, chatId);
            case NOTIFICATION_COMMAND -> notificationBotCommand.execute(userId, chatId);
            default -> unknownBotCommand.execute(userId, chatId);
        };
    }
}