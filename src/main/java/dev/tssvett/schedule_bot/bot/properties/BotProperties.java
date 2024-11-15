package dev.tssvett.schedule_bot.bot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotProperties(
        String name,
        String token
) {
}
