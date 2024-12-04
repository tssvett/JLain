package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.parsing.parser.GroupParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.GroupRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @InjectMocks
    GroupService groupService;
    @Mock
    GroupParser groupParser;
    @Mock
    FacultyService facultyService;
    @Mock
    GroupRepository groupRepository;
    @Mock
    StudentRepository studentRepository;

    private EducationalGroupRecord expectedGroup;
    private FacultyRecord expectedFaculty;
    private List<EducationalGroupRecord> expectedGroups;
    private List<FacultyRecord> expectedFaculties;

    private StudentRecord student;

    @BeforeEach
    void setUp() {
        expectedGroup = new EducationalGroupRecord(1L, "group", 1L, 1L);
        expectedGroups = List.of(expectedGroup);

        expectedFaculty = new FacultyRecord(1L, "faculty");
        expectedFaculties = List.of(expectedFaculty);

        student = new StudentRecord(1L, 1L, 1L, "state",
                1L, 1L, 1L, "role");
        groupService = new GroupService(groupParser, facultyService, groupRepository, studentRepository);
    }

    @Test
    void getFilteredByCourseAndFacultyGroups_idOfExistingStudent_returnsGroups() {        // Arrange
        Long studentId = 3L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(groupRepository.findAllByFacultyIdAndCourse(student.getFacultyId(), student.getCourse()))
                .thenReturn(expectedGroups);

        // Act
        List<EducationalGroupRecord> actualGroups = groupService.getFilteredByCourseAndFacultyGroups(studentId);

        // Assert
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    void getFilteredByCourseAndFacultyGroups_notExistingStudentId_throwsStudentNotExistsException() {        // Arrange
        Long notExistingId = 4L;
        when(studentRepository.findById(notExistingId)).thenThrow(StudentNotExistsException.class);
        // Act && Assert
        assertThrows(StudentNotExistsException.class,
                () -> groupService.getFilteredByCourseAndFacultyGroups(notExistingId));
    }

    @Test
    void saveGroups() {
        //Arrange
        doNothing().when(groupRepository).saveAll(expectedGroups);
        //Act
        groupService.saveGroups(expectedGroups);
        //Assert
        verify(groupRepository).saveAll(expectedGroups);
    }

    @Test
    void findAllGroups() {
        //Arrange
        when(groupRepository.findAll()).thenReturn(expectedGroups);
        //Act
        List<EducationalGroupRecord> allGroups = groupService.findAllGroups();
        //Assert
        verify(groupRepository).findAll();
        assertEquals(expectedGroups, allGroups);
    }

    @Test
    void getGroupById_existingGroupId_returnsGroup() {
        //Arrange
        Long groupId = 1L;
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(expectedGroup));
        //Act
        EducationalGroupRecord actualGroup = groupService.getGroupById(groupId);
        //Assert
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void getGroupById_notExistingGroupId_throwsException() {
        //Arrange
        Long notExistingGroupId = 2L;
        when(groupRepository.findById(notExistingGroupId)).thenReturn(Optional.empty());
        //Act && Assert
        assertThrows(StudentNotExistsException.class,
                () -> groupService.getGroupById(notExistingGroupId));
    }

    @Test
    void parseGroupsFromAllFaculties_facultiesWithNoRelations_returnsEmptyList() {
        //Arrange
        when(facultyService.findAllFaculties()).thenReturn(expectedFaculties);
        //Act
        List<EducationalGroupRecord> actualGroups = groupService.parseGroupsFromAllFaculties();
        //Assert
        assertEquals(List.of(), actualGroups);
    }
}