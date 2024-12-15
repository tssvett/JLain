package dev.tssvett.schedule_bot.parsing.flow;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParserFlow {
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final LessonService lessonService;

    public void startParsingFlow() {
        log.debug("Starting parsing flow");
        facultyService.parseAndSaveFaculties();
        groupService.parseAndSaveGroups();
        lessonService.parseAndSaveLessonsFromAllGroupsCompletableFuture();
        log.debug("Parsing flow finished");
    }
}
