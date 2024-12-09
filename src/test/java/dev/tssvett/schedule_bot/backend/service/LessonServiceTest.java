package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.parsing.dto.LessonParserDto;
import dev.tssvett.schedule_bot.parsing.parser.LessonParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.LessonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private GroupService groupService;

    @Mock
    private LessonParser lessonParser;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private DateUtils dateUtils;

    @InjectMocks
    private LessonService lessonService;

    private static final Long userId = 123L;
    private static final Long groupId = 456L;
    private LessonRecord lessonRecord;
    private List<LessonRecord> lessonRecords;
    private LessonParserDto lessonParserDto;
    private StudentRecord studentRecord;
    private EducationalGroupRecord educationalGroupRecord;

    @BeforeEach
    void setUp() {
        Long notificationId = 999L;
        Long course = 3L;
        Long facultyId = 789L;
        lessonRecord = new LessonRecord(UUID.randomUUID(), "Math", "лекция", "Room 101",
                "Teacher", "1", "8:00", "monday", "04.12.2024", groupId, 1L);
        lessonRecords = List.of(lessonRecord, lessonRecord);
        lessonParserDto = new LessonParserDto(UUID.randomUUID(), "Math", "лекция", "Room 101",
                "Teacher", "1", "8:00", "monday", "04.12.2024", groupId, 1L);
        studentRecord = new StudentRecord(userId, userId, course, RegistrationState.FACULTY_CHOOSING.name(),
                facultyId, groupId, notificationId, Role.STUDENT.name());
        educationalGroupRecord = new EducationalGroupRecord(groupId, "Math", course, facultyId);
    }

    @Test
    void getWeekScheduleMapByDate() {
        //Arrange
        when(studentService.getStudentInfoById(userId)).thenReturn(studentRecord);
        when(lessonRepository.findLessonsByGroupIdAndEducationalWeek(groupId, 1L)).thenReturn(lessonRecords);
        when(dateUtils.calculateCurrentUniversityEducationalWeek()).thenReturn(1);
        //Act
        Map<String, List<LessonInfoDto>> weekScheduleMapByDate = lessonService.getWeekScheduleMapByDate(userId);

        //Assert
        verify(studentService).getStudentInfoById(userId);
        verify(lessonRepository).findLessonsByGroupIdAndEducationalWeek(groupId, 1L);
        assertEquals(1, weekScheduleMapByDate.entrySet().size());
        assertEquals(2, weekScheduleMapByDate.get("monday").size());
    }


    @Test
    void parseLessonsFromAllGroups() {
        List<EducationalGroupRecord> groups = List.of(educationalGroupRecord, educationalGroupRecord, educationalGroupRecord);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(dateUtils.calculateCurrentUniversityEducationalWeek()).thenReturn(1);
        when(lessonParser.parse(anyLong(), anyInt())).thenReturn(new ArrayList<>());
        List<LessonRecord> result = lessonService.parseLessonsFromAllGroups();
        assertEquals(0, result.size());
    }

    @Test
    void parseAndSaveLessonsFromAllGroupsCompletableFuture() {
        List<EducationalGroupRecord> groups = List.of(educationalGroupRecord, educationalGroupRecord, educationalGroupRecord);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(dateUtils.calculateCurrentUniversityEducationalWeek()).thenReturn(1);
        when(lessonParser.parse(anyLong(), anyInt())).thenReturn(new ArrayList<>());
        lessonService.parseAndSaveLessonsFromAllGroupsCompletableFuture();
        verify(lessonParser, times(3)).parse(anyLong(), anyInt());
    }

    @Test
    void saveLessonsWithoutDuplication() {
        when(lessonRepository.findAllLessons()).thenReturn(List.of(lessonRecord));
        lessonService.saveLessonsWithoutDuplication(List.of(lessonRecord));
        verify(lessonRepository).saveAll(any(List.class));
    }

    @Test
    void findScheduleDifference() {
        List<LessonRecord> dbLessons = List.of(lessonRecord);
        List<LessonParserDto> parsedLessons = List.of(lessonParserDto);
        when(lessonRepository.findLessonsByGroupIdAndEducationalDayNumber(groupId, "04.12.2024"))
                .thenReturn(dbLessons);
        when(studentService.getStudentInfoById(userId)).thenReturn(studentRecord);
        when(dateUtils.getCurrentDate()).thenReturn("04.12.2024");
        when(dateUtils.calculateCurrentUniversityEducationalWeek()).thenReturn(1);
        when(lessonParser.parse(anyLong(), anyInt())).thenReturn(parsedLessons);
        lessonService.findScheduleDifference(userId);
        verify(lessonRepository, times(1)).findLessonsByGroupIdAndEducationalDayNumber(
                anyLong(), any());
    }
}