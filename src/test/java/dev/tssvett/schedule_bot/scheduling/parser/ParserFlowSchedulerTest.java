package dev.tssvett.schedule_bot.scheduling.parser;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.LessonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParserFlowSchedulerTest {

    @Mock
    FacultyService facultyService;

    @Mock
    GroupService groupService;

    @Mock
    LessonService lessonService;

    @InjectMocks
    ParserFlowScheduler parserFlowScheduler;

    @Test
    void startParsingFlow() {
        // Arrange
        doNothing().when(facultyService).parseAndSaveFaculties();
        doNothing().when(groupService).parseAndSaveGroups();
        doNothing().when(lessonService).parseAndSaveLessonsFromAllGroupsCompletableFuture();

        // Act
        parserFlowScheduler.startParsingFlow();

        // Assert
        verify(facultyService).parseAndSaveFaculties();
        verify(groupService).parseAndSaveGroups();
        verify(lessonService).parseAndSaveLessonsFromAllGroupsCompletableFuture();
    }
}