package dev.tssvett.schedule_bot.scheduling.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "scheduling")
public class SchedulingProperties {
    private Long delay;
    private Boolean enabled;
}
