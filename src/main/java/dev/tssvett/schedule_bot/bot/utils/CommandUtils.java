package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.enums.constants.CommandNames;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class CommandUtils {

    public static String parseCommandFromMessage(String command) {
        int atIndex = command.indexOf("@");
        if (atIndex != -1) {
            return command.substring(0, atIndex);
        } else {
            return command;
        }
    }

    public static Optional<CommandNames> convertStringCommandToEnumValue(String command) {
        try {
            return Optional.of(CommandNames.valueOf(command));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
