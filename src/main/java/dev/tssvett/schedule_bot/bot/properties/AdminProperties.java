package dev.tssvett.schedule_bot.bot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(
        Long id
) {
}
