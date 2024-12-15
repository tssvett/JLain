package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.LessonService;
import dev.tssvett.schedule_bot.parsing.flow.ParserFlow;
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
    private final ParserFlow parserFlow;

    @Scheduled(cron = "${scheduling.parser.cron}")
    public void startParsingFlow() {
        log.info("Staring scheduled parsing flow");
        parserFlow.startParsingFlow();
        log.info("Scheduled parsing flow finished");
    }
}
