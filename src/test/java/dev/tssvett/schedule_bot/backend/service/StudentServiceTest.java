package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.properties.AdminProperties;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentRepository studentRepository;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    private AdminProperties adminProperties;

    private StudentService studentService;

    private Long studentId = 1L;
    private StudentRecord studentRecord;
    private NotificationRecord notificationRecord;
    private List<StudentRecord> studentRecords;


    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, notificationRepository, adminProperties);
        studentRecord = new StudentRecord(studentId, 1L, 3L, "START_REGISTER", 123L,
                123L, 1L, "STUDENT");
        studentRecords = List.of(studentRecord);
        notificationRecord = new NotificationRecord(null, true, studentId, true);
    }

    @Test
    void getStudentInfoById() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));

        //Act
        StudentRecord actual = studentService.getStudentInfoById(studentId);

        //Assert
        assertEquals(studentRecord, actual);
    }

    @Test
    void findAll() {
        //Arrange
        when(studentRepository.findAll()).thenReturn(studentRecords);
        //Act
        List<StudentRecord> actual = studentService.findAll();
        //Assert
        assertEquals(studentRecords, actual);
    }

    @Test
    void updateStudentFaculty_happyPath() {
        //Arrange
        studentRecord.setRegistrationState("FACULTY_CHOOSING");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        doNothing().when(studentRepository).updateAllFields(any(), any());
        //Act
        studentService.updateStudentFaculty(studentId, 2L);
        //Assert
        assertEquals(2L, studentRecord.getFacultyId());
    }

    @Test
    void updateStudentFaculty_wrongState() {
        //Arrange
        studentRecord.setRegistrationState("START_REGISTER");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act && Assert
        assertThrows(NotValidRegistrationStateException.class, () -> studentService.updateStudentFaculty(studentId, 2L));
    }

    @Test
    void updateStudentCourse_happyPath() {
        //Arrange
        studentRecord.setRegistrationState("COURSE_CHOOSING");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        doNothing().when(studentRepository).updateAllFields(any(), any());
        //Act
        studentService.updateStudentCourse(studentId, 2L);
        //Assert
        assertEquals(2L, studentRecord.getCourse());
    }

    @Test
    void updateStudentCourse_wrongState() {
        //Arrange
        studentRecord.setRegistrationState("START_REGISTER");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act && Assert
        assertThrows(NotValidRegistrationStateException.class, () -> studentService.updateStudentCourse(studentId, 2L));
    }

    @Test
    void updateStudentGroup_happyPath() {
        //Arrange
        studentRecord.setRegistrationState("GROUP_CHOOSING");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        doNothing().when(studentRepository).updateAllFields(any(), any());
        //Act
        studentService.updateStudentGroup(studentId, 2L);
        //Assert
        assertEquals(2L, studentRecord.getGroupId());
    }

    @Test
    void updateStudentGroup_wrongState() {
        //Arrange
        studentRecord.setRegistrationState("START_REGISTER");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act && Assert
        assertThrows(NotValidRegistrationStateException.class, () -> studentService.updateStudentGroup(studentId, 2L));
    }

    @Test
    void updateTomorrowScheduleNotificationStatus_happyPath() {
        //Arrange
        studentRecord.setRegistrationState("SUCCESSFUL_REGISTRATION");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        doNothing().when(notificationRepository).updateTomorrowScheduleStatus(any(), any());
        //Act
        studentService.updateTomorrowScheduleNotificationStatus(studentId, true);
        //Assert
        assertEquals(true, notificationRecord.getTomorrowScheduleEnabled());
    }

    @Test
    void updateTomorrowScheduleNotificationStatus_wrongState() {
        //Arrange
        studentRecord.setRegistrationState("START_REGISTER");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act && Assert
        assertThrows(NotValidRegistrationStateException.class, () -> studentService.updateTomorrowScheduleNotificationStatus(studentId, true));
    }

    @Test
    void updateScheduleDifferenceNotificationStatus_happyPath() {
        //Arrange
        studentRecord.setRegistrationState("SUCCESSFUL_REGISTRATION");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        doNothing().when(notificationRepository).updateScheduleDifferenceStatus(any(), any());
        //Act
        studentService.updateScheduleDifferenceNotificationStatus(studentId, true);
        //Assert
        assertEquals(true, notificationRecord.getScheduleDifferenceEnabled());
    }

    @Test
    void updateScheduleDifferenceNotificationStatus_wrongState() {
        //Arrange
        studentRecord.setRegistrationState("START_REGISTER");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act && Assert
        assertThrows(NotValidRegistrationStateException.class, () -> studentService.updateScheduleDifferenceNotificationStatus(studentId, true));
    }

    @Test
    void updateRegistrationState() {
        //Arrange
        doNothing().when(studentRepository).updateState(any(), any());

        //Act
        studentService.updateStudentRegistrationState(studentId, RegistrationState.SUCCESSFUL_REGISTRATION);

        //Assert
        verify(studentRepository).updateState(studentId, RegistrationState.SUCCESSFUL_REGISTRATION.name());
    }

    @Test
    void createUserIfNotExists_happyPath() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        when(adminProperties.id()).thenReturn(studentId);
        doNothing().when(studentRepository).save(any(StudentRecord.class));
        when(notificationRepository.save(any(NotificationRecord.class))).thenReturn(notificationRecord);
        doNothing().when(studentRepository).updateNotificationId(any(), any());

        //Act
        studentService.createStudentIfNotExists(studentId, studentId);
        //Assert
        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(any(StudentRecord.class));
        verify(notificationRepository).save(any(NotificationRecord.class));
        verify(studentRepository).updateNotificationId(any(), any());
    }

    @Test
    void createUserIfNotExists_userExists() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));

        //Act
        studentService.createStudentIfNotExists(studentId, studentId);
        //Assert
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(notificationRepository);
    }

    @Test
    void isRegistered() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act
        boolean isRegistered = studentService.isRegistered(studentId);
        //Assert
        assertFalse(isRegistered);
    }

    @Test
    void isTomorrowScheduleEnabled() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        when(notificationRepository.findById(studentId)).thenReturn(Optional.of(notificationRecord));
        //Act
        boolean isTomorrowScheduleEnabled = studentService.isTomorrowScheduleNotificationEnabled(studentId);
        //Assert
        assertTrue(isTomorrowScheduleEnabled);
    }

    @Test
    void isScheduleDifferenceEnabled() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        when(notificationRepository.findById(studentId)).thenReturn(Optional.of(notificationRecord));
        //Act
        boolean isScheduleDifferenceEnabled = studentService.isScheduleDifferenceNotificationEnabled(studentId);
        //Assert
        assertTrue(isScheduleDifferenceEnabled);
    }

    @Test
    void isAdmin() {
        //Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentRecord));
        //Act
        boolean isAdmin = studentService.isAdmin(studentId);
        //Assert
        assertFalse(isAdmin);
    }
}