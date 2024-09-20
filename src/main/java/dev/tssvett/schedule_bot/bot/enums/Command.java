package dev.tssvett.schedule_bot.bot.enums;

import dev.tssvett.schedule_bot.bot.actions.command.constants.CommandConstants;
import lombok.Getter;

import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.HELP_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.INFO_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.NOTIFICATION_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.PICTURE_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.REGISTER_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.START_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.TODAY_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.TOMORROW_DESCRIPTION;
import static dev.tssvett.schedule_bot.bot.actions.command.constants.CommandDescriptionConstants.WEEK_DESCRIPTION;


@Getter
public enum Command {
    TODAY(CommandConstants.TODAY, TODAY_DESCRIPTION),
    TOMORROW(CommandConstants.TOMORROW, TOMORROW_DESCRIPTION),
    WEEK(CommandConstants.WEEK, WEEK_DESCRIPTION),
    REGISTER(CommandConstants.REGISTER, REGISTER_DESCRIPTION),
    START(CommandConstants.START, START_DESCRIPTION),
    HELP(CommandConstants.HELP, HELP_DESCRIPTION),
    PICTURE(CommandConstants.PICTURE, PICTURE_DESCRIPTION),
    INFO(CommandConstants.INFO, INFO_DESCRIPTION),
    NOTIFICATION(CommandConstants.NOTIFICATION, NOTIFICATION_DESCRIPTION);

    private final String commandName;
    private final String description;

    Command(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }
}
