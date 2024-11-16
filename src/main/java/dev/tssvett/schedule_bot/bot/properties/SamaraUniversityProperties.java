package dev.tssvett.schedule_bot.bot.properties;

import java.time.LocalDate;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("samara-university")
public record SamaraUniversityProperties(
        LocalDate semesterStartDate,
        String facultyUrl,
        String groupUrl,
        String lessonUrl
) {
}
