package dev.tssvett.schedule_bot.bot.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
@UtilityClass
public class DateUtils {

    public static Integer calculateWeekNumber() {
        LocalDate now = LocalDate.now();
        LocalDate semesterStart = LocalDate.of(2024, 9, 2);
        log.debug("Current Week number: {}", (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1);

        return (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1;
    }

    public static String calculateCurrentDayName() {
        LocalDate now = LocalDate.now();
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.debug("Current day name: {}", dayName);
        return dayName;
    }

    public static String calculateTomorrowDayName() {
        LocalDate now = LocalDate.now().plusDays(1);
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.debug("Current day name: {}", dayName);

        return dayName;
    }
}
