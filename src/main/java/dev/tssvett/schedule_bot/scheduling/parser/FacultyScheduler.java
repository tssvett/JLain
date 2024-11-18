package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.parsing.parser.FacultyParser;
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
@ConditionalOnProperty(value = "scheduling.faculty.enabled", havingValue = "true")
public class FacultyScheduler {
    private final FacultyService facultyService;
    private final FacultyParser facultyParser;

    @Scheduled(cron = "${scheduling.faculty.cron}")
    public void startParsingAndSavingFacultiesToDatabase() {
        log.info("Faculty Scheduler started");

        facultyService.saveFaculties(
                facultyParser.parse()
                        .stream()
                        .map(Mapper::toFacultyRecord)
                        .toList()
        );

        log.info("Faculty Scheduler finished");
    }
}
