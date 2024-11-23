package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
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
@ConditionalOnProperty(value = "scheduling.parser.enabled", matchIfMissing = true)
public class ParserFlowScheduler {
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final LessonService lessonService;

    @Scheduled(cron = "${scheduling.parser.cron}")
    public void startParsingFlow() {
        log.info("Starting parsing flow");

        facultyService.parseAndSaveFaculties();

        groupService.parseAndSaveGroups();

        lessonService.parseAndSaveLessonsFromAllGroupsCompletableFuture();

        log.info("Parsing flow finished");
    }
}
