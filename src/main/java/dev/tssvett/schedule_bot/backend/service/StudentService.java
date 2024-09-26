package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.COURSE_CHOOSING;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.GROUP_CHOOSING;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
    }

    public Student updateStudentFaculty(Long studentId, Faculty faculty) {
        return proceedRegistrationState(studentId, faculty, RegistrationState.FACULTY_CHOOSING,
                RegistrationState.COURSE_CHOOSING, studentRepository::save);
    }

    public Student updateStudentCourse(Long studentId, Long course) {
        return proceedRegistrationState(studentId, course, COURSE_CHOOSING, RegistrationState.GROUP_CHOOSING,
                studentRepository::save);
    }

    public Student updateStudentGroup(Long studentId, Group group) {
        return proceedRegistrationState(studentId, group, GROUP_CHOOSING, RegistrationState.SUCCESSFUL_REGISTRATION,
                studentRepository::save);
    }

    public Student updateStudentNotification(Long studentId, Notification notification) {
        return proceedRegistrationState(studentId, notification, RegistrationState.SUCCESSFUL_REGISTRATION,
                RegistrationState.SUCCESSFUL_REGISTRATION, studentRepository::save);
    }

    public Student updateStudentRegistrationState(Long studentId, RegistrationState state) {
        Student student = this.findStudentById(studentId);
        student.setRegistrationState(state);
        log.info("Student {} updated state to {}", studentId, state);

        return studentRepository.save(student);
    }

    @Transactional
    public Student createStudentIfNotExists(Long studentId, Long chatId) {
        return studentRepository.findById(studentId)
                .orElseGet(() -> createStudent(studentId, chatId));
    }

    @Transactional
    public Student createStudent(Long studentId, Long chatId) {
        log.info("Student {} is not in database. Adding them to database", studentId);

        Notification notification = Notification.builder()
                .enabled(true)
                .build();

        Student newStudent = Student.builder()
                .userId(studentId)
                .chatId(chatId)
                .registrationState(START_REGISTER)
                .notification(notification)
                .build();

        notification.setStudent(newStudent);
        return studentRepository.save(newStudent);
    }

    public Boolean isExist(Long studentId) {
        return studentRepository.findById(studentId).isPresent();
    }

    public Boolean isRegistered(Long studentId) {
        return studentRepository.findById(studentId)
                .map(student -> student.getRegistrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION))
                .orElse(false);
    }

    private <T> void validateRegistrationState(Student student, T entity, RegistrationState state) {
        if (!student.getRegistrationState().equals(state)) {
            throw new NotValidRegistrationStateException(String.format("Student click on %s with wrong state %s",
                    entity, student.getRegistrationState()));
        }
    }

    private Student proceedRegistrationState(Long studentId, Object newValue, RegistrationState validState, RegistrationState newState, Consumer<Student> setter) {
        Student student = this.findStudentById(studentId);
        validateRegistrationState(student, newValue, validState);

        setter.accept(student);
        student.setRegistrationState(newState);
        log.info("Student {} updated state to {} with value {}", studentId, newState, newValue);

        return studentRepository.save(student);
    }
}
