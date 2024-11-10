package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;


    public StudentInfoDto getStudentInfoById(Long studentId) {
        StudentRecord student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));

        return Mapper.toStudentInfoDto(student);
    }

    public void updateStudentFaculty(Long studentId, Long facultyId) {
        proceedRegistrationState(studentId, RegistrationState.FACULTY_CHOOSING, RegistrationState.COURSE_CHOOSING,
                student -> student.setFacultyId(facultyId));
    }

    public void updateStudentCourse(Long studentId, Long course) {
        proceedRegistrationState(studentId, RegistrationState.COURSE_CHOOSING,
                RegistrationState.GROUP_CHOOSING, student -> student.setCourse(course));
    }

    public void updateStudentGroup(Long studentId, Long groupId) {
        proceedRegistrationState(studentId, RegistrationState.GROUP_CHOOSING,
                RegistrationState.SUCCESSFUL_REGISTRATION, student -> student.setGroupId(groupId));
    }

    public void updateStudentNotification(Long studentId, Boolean notificationStatus) {
        Long notificationId = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId))
                .getNotificationId();

        notificationRepository.update(notificationId, notificationStatus);

        proceedRegistrationState(studentId, RegistrationState.SUCCESSFUL_REGISTRATION,
                RegistrationState.SUCCESSFUL_REGISTRATION, student -> student.setNotificationId(notificationId));
    }

    public void updateStudentRegistrationState(Long studentId, RegistrationState state) {
        log.debug("Student {} updated state to {}", studentId, state);
        studentRepository.updateState(studentId, state.name());
    }

    public void createStudentIfNotExists(Long studentId, Long chatId) {
        studentRepository.findById(studentId)
                .orElseGet(() -> createStudent(studentId, chatId));
    }


    public Boolean isRegistered(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId))
                .getRegistrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION.toString());
    }

    private void validateRegistrationState(StudentRecord student, RegistrationState state) {
        if (!student.getRegistrationState().equals(state.name())) {
            throw new NotValidRegistrationStateException(String.format("Student click on wrong button." +
                    " Student in wrong state %s", student.getRegistrationState()));
        }
    }

    private void proceedRegistrationState(Long studentId, RegistrationState validState,
                                          RegistrationState newState, Consumer<StudentRecord> setter) {
        StudentRecord student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));

        validateRegistrationState(student, validState);

        student.setRegistrationState(newState.name());
        setter.accept(student);

        studentRepository.updateAll(student.getUserId(), student);
        log.debug("Student {} updated state to {}", studentId, newState);
    }

    private StudentRecord createStudent(Long studentId, Long chatId) {
        log.info("Student {} is not in database. Adding them to database", studentId);

        NotificationRecord notification = new NotificationRecord(null, true, null);

        StudentRecord newStudent = new StudentRecord(
                studentId,
                chatId,
                null,
                START_REGISTER.name(),
                null,
                null,
                notification.getId()
        );

        notification.setUserId(newStudent.getUserId());
        notificationRepository.save(notification);
        studentRepository.save(newStudent);

        return newStudent;
    }
}
