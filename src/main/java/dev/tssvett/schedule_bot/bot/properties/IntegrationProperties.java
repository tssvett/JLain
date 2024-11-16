package dev.tssvett.schedule_bot.bot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration")
public record IntegrationProperties(
        Integer maxRetries,
        Integer retryDelay
) {
}
