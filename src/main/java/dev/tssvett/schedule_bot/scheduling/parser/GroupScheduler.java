package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.service.GroupService;
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
@ConditionalOnProperty(name = "scheduling.group.enabled", havingValue = "true")
public class GroupScheduler {
    private final GroupService groupService;

    @Scheduled(cron= "${scheduling.group.cron}")
    public void startParsingAndSavingGroupsToDatabase() {
        log.info("Starting parsing all groups");

        groupService.saveGroups(
                groupService.parseGroupsFromAllFaculties()
        );

        log.info("Parsing completed successfully! Total groups in database: {}", groupService.findAllGroups().size());
    }
}
