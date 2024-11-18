package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scheduling.lesson.enabled", havingValue = "true")
public class LessonScheduler {
    private final LessonService lessonService;

    @Scheduled(fixedDelayString = "${scheduling.lesson.delay}")
    public void startParsingAndSavingFacultiesToDatabase() {
        log.info("Lesson Scheduler started");

        //TODO: подумай, что будет когда парсер опять будет работать
        lessonService.parseAndSaveLessonsFromAllGroupsCompletableFuture();

        log.info("Lesson Scheduler finished");
    }
}
