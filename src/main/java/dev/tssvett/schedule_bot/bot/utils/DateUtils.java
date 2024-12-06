package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.properties.SamaraUniversityProperties;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DateUtils {
    private final SamaraUniversityProperties properties;
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    public Integer calculateCurrentUniversityEducationalWeek() {
        LocalDate now = LocalDate.now();
        LocalDate semesterStart = properties.semesterStartDate();
        log.debug("Current Week number: {}", (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1);

        return (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1;
    }

    public Integer calculateCurrentUniversityEducationalWeek(int deltaWeek) {
        LocalDate now = LocalDate.now();
        LocalDate semesterStart = properties.semesterStartDate();
        log.debug("Current Week number: {}", (int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1);

        return ((int) (now.toEpochDay() - semesterStart.toEpochDay()) / 7 + 1) + deltaWeek;
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

    public String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public String getTomorrowDate() {
        return LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public String getYesterdayDate() {
        return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

}
