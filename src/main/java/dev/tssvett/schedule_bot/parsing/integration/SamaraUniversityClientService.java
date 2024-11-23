package dev.tssvett.schedule_bot.parsing.integration;

import dev.tssvett.schedule_bot.backend.exception.parse.SamaraUniversityConnectionException;
import dev.tssvett.schedule_bot.bot.properties.IntegrationProperties;
import dev.tssvett.schedule_bot.bot.properties.SamaraUniversityProperties;
import dev.tssvett.schedule_bot.parsing.enums.Connection;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@EnableRetry
@Component
@RequiredArgsConstructor
public class SamaraUniversityClientService {
    private final SamaraUniversityProperties samaraUniversityProperties;
    private final IntegrationProperties integrationProperties;

    @Retryable(
            maxAttemptsExpression = "${integration.max-retries}",
            backoff = @Backoff(delayExpression = "${integration.retry-delay}"),
            retryFor = {SamaraUniversityConnectionException.class}
    )
    @CircuitBreaker(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    @RateLimiter(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    public Optional<Document> getFacultiesHtml() {
        String url = samaraUniversityProperties.facultyUrl();

        return fetchDocument(url);
    }

    @Retryable(
            maxAttemptsExpression = "${integration.max-retries}",
            backoff = @Backoff(delayExpression = "${integration.retry-delay}"),
            retryFor = {SamaraUniversityConnectionException.class}
    )
    @CircuitBreaker(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    @RateLimiter(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    public Optional<Document> getGroupsHtml(long facultyId, long course) {
        String url = String.format(samaraUniversityProperties.groupUrl(), facultyId, course);

        return fetchDocument(url);
    }

    @Retryable(
            maxAttemptsExpression = "${integration.max-retries}",
            backoff = @Backoff(delayExpression = "${integration.retry-delay}"),
            retryFor = {SamaraUniversityConnectionException.class}
    )
    @CircuitBreaker(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    @RateLimiter(name = "samaraUniversityClientService", fallbackMethod = "fallbackGetHtml")
    public Optional<Document> getLessonsHtml(Long groupId, Integer week) {
        String url = String.format(samaraUniversityProperties.lessonUrl(), groupId, week);

        return fetchDocument(url);
    }

    private Optional<Document> fetchDocument(String url) {
        try {
            return Optional.ofNullable(
                    Jsoup.connect(url)
                            .userAgent(Connection.USER_AGENT.getName())
                            .timeout(integrationProperties.timeout())
                            .get()
            );
        } catch (IOException e) {
            throw new SamaraUniversityConnectionException(e.getMessage());
        }
    }

    private Optional<Document> fallbackGetHtml(Throwable throwable) {
        log.error("Circuit breaker triggered for SamaraUniversityClientService. Error message: {}", throwable.getMessage());

        return Optional.empty();
    }
}
