package dev.tssvett.schedule_bot.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
