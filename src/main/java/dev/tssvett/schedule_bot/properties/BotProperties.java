package dev.tssvett.schedule_bot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bot")
@Data
public class BotProperties {
    String name;
    String token;
}
