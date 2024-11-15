package dev.tssvett.schedule_bot.bot.properties;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("samara-university")
public class SamaraUniversityProperties {
    LocalDate semesterStartDate;
    String facultyUrl;
    String groupUrl;
    String lessonUrl;
}
