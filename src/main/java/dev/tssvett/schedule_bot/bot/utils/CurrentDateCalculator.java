package dev.tssvett.schedule_bot.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
@Component
public class CurrentDateCalculator {

    public Integer calculateWeekNumber() {
        LocalDate now = LocalDate.now();
        LocalDate semesterStart = LocalDate.of(2024, 9, 2);
        log.info("Current Week number: {}", (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1);
        return (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1;
    }
    public String calculateCurrentDayName() {
        LocalDate now = LocalDate.now();
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.info("Current day name: {}", dayName);
        return dayName;
    }

    public String calculateTomorrowDayName() {
        LocalDate now = LocalDate.now().plusDays(1);
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.info("Current day name: {}", dayName);
        return dayName;
    }
}
