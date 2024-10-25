package dev.tssvett.schedule_bot.bot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
@ConfigurationProperties("samara-university")
public class SamaraUniversityProperties {
    LocalDate semesterStartDate;
}
