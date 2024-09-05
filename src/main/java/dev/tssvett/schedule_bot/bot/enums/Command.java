package dev.tssvett.schedule_bot.bot.enums;

import dev.tssvett.schedule_bot.bot.constants.CommandConstants;
import lombok.Getter;

import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.HELP_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.INFO_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.NOTIFICATION_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.PICTURE_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.REGISTER_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.START_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.TODAY_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.TOMORROW_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.constants.CommandDescriptionConstants.WEEK_DESCRIPTION;

@Getter
public enum Command {
    TODAY(CommandConstants.TODAY, TODAY_DESCRIPTION),
    TOMMORROW(CommandConstants.TOMORROW, TOMORROW_DESCRIPTION),
    WEEK(CommandConstants.WEEK, WEEK_DESCRIPTION),
    REGISTER(CommandConstants.REGISTER, REGISTER_DESCRIPTION),
    START(CommandConstants.START, START_DESCRIPTION),
    HELP(CommandConstants.HELP, HELP_DESCRIPTION),
    PICTURE(CommandConstants.PICTURE, PICTURE_DESCRIPTION),
    GADIT(CommandConstants.GADIT, "null"),
    INFO(CommandConstants.INFO, INFO_DESCRIPTION),
    NOTIFICATION(CommandConstants.NOTIFICATION, NOTIFICATION_DESCRIPTION);

    private final String commandName;
    private final String description;

    Command(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }
}
