package dev.tssvett.schedule_bot.enums;

import dev.tssvett.schedule_bot.constants.CommandConstants;
import lombok.Getter;

import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.HELP_DESCRIPTION;
import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.PICTURE_DESCRIPTION;
import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.REGISTER_DESCRIPTION;
import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.SCHEDULE_DESCRIPTION;
import static dev.tssvett.schedule_bot.constants.CommandDescriptionConstants.START_DESCRIPTION;

@Getter
public enum Command {
    START(CommandConstants.START, START_DESCRIPTION),
    HELP(CommandConstants.HELP, HELP_DESCRIPTION),
    PICTURE(CommandConstants.PICTURE, PICTURE_DESCRIPTION),
    SCHEDULE(CommandConstants.SCHEDULE, SCHEDULE_DESCRIPTION),
    REGISTER(CommandConstants.REGISTER, REGISTER_DESCRIPTION);


    private final String commandName;
    private final String description;

    Command(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }
}
