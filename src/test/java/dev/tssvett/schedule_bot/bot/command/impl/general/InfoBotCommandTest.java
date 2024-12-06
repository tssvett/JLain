package dev.tssvett.schedule_bot.bot.command.impl.general;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InfoBotCommandTest {
    @Mock
    StudentService studentService;

    @Mock
    FacultyService facultyService;

    @Mock
    GroupService groupService;

    @InjectMocks
    InfoBotCommand infoBotCommand;

    private StudentRecord studentRecord;
    private FacultyRecord facultyRecord;
    private EducationalGroupRecord groupRecord;


    @BeforeEach
    void setUp() {
        studentRecord = new StudentRecord(1L, 1L, 3L, "FACULTY_CHOOSING", 1L,
                1L, 1L, "STUDENT");
        facultyRecord = new FacultyRecord(1L, "faculty");
        groupRecord = new EducationalGroupRecord(1L, "group", 3L, 1L);
    }


    @Test
    void execute_happyPath() {
        //Arrange
        when(studentService.getStudentInfoById(1L)).thenReturn(studentRecord);
        when(facultyService.getFacultyById(1L)).thenReturn(facultyRecord);
        when(groupService.getGroupById(1L)).thenReturn(groupRecord);
        when(studentService.isTomorrowScheduleNotificationEnabled(1L)).thenReturn(true);
        when(studentService.isScheduleDifferenceNotificationEnabled(1L)).thenReturn(true);

        //Act
        infoBotCommand.execute(1L, 1L);

        //Assert
        verify(studentService).getStudentInfoById(1L);
        verify(facultyService).getFacultyById(1L);
        verify(groupService).getGroupById(1L);
        verify(studentService).isTomorrowScheduleNotificationEnabled(1L);
        verify(studentService).isScheduleDifferenceNotificationEnabled(1L);
    }
}