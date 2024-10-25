package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.properties.SamaraUniversityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class DateUtils {
    private final SamaraUniversityProperties properties;

    public Integer calculateCurrentUniversityEducationalWeek() {
        LocalDate now = LocalDate.now();
        LocalDate semesterStart = properties.getSemesterStartDate();
        log.debug("Current Week number: {}", (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1);

        return (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1;
    }

    public String calculateCurrentDayName() {
        LocalDate now = LocalDate.now();
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.debug("Current day name: {}", dayName);
        return dayName;
    }

    public String calculateTomorrowDayName() {
        LocalDate now = LocalDate.now().plusDays(1);
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru")).toLowerCase();
        log.debug("Current day name: {}", dayName);

        return dayName;
    }
}
