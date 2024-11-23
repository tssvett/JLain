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

    /**
     * Для формата времени [8.00, 9,45]. Формат справедливен для всей недели(единый)
     *
     * @param timeParts Массив строк
     * @return Время урока по дням недели, в формате 8.00 - 9.45
     */
    public static String extractUnifiedTime(String[] timeParts) {
        return timeParts[0] + " - " + timeParts[1];
    }

    /**
     * @param timeParts Массив строк
     * @return Время уроков по дням недели, в формате Пн-Пт 8.00 - 9.45, Сб 8.00 - 9.45
     */
    public static String extractNotUnifiedTime(String[] timeParts) {
        return String.format("%s %s - %s, %s %s - %s", timeParts[0], timeParts[1], timeParts[2], timeParts[3],
                timeParts[4], timeParts[5]);
    }
}
