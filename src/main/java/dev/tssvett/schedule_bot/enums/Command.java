package dev.tssvett.schedule_bot.enums;

import dev.tssvett.schedule_bot.constants.CommandConstants;
import lombok.Getter;

import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.*;

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
