package dev.tssvett.schedule_bot.bot.utils;

import lombok.experimental.UtilityClass;

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
}
