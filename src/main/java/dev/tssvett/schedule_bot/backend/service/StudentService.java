package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.backend.exception.database.FacultyNotExistException;
import dev.tssvett.schedule_bot.backend.exception.database.GroupNotExistException;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.persistence.repository.FacultyRepository;
import dev.tssvett.schedule_bot.persistence.repository.GroupRepository;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final GroupRepository groupRepository;
    private final NotificationRepository notificationRepository;

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
    }

    public StudentInfoDto getStudentInfoById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));

        return Mapper.toStudentInfoDto(student);
    }

    public void updateStudentFaculty(Long studentId, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotExistException("Faculties with id: " + facultyId + " not found"));
        proceedRegistrationState(studentId, faculty, RegistrationState.FACULTY_CHOOSING, RegistrationState.COURSE_CHOOSING,
                student -> student.setFaculty(faculty));
    }

    public void updateStudentCourse(Long studentId, Long course) {
        proceedRegistrationState(studentId, course, RegistrationState.COURSE_CHOOSING,
                RegistrationState.GROUP_CHOOSING, student -> student.setCourse(course));
    }

    public void updateStudentGroup(Long studentId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotExistException("Group with id: " + groupId + " not found"));
        proceedRegistrationState(studentId, group, RegistrationState.GROUP_CHOOSING,
                RegistrationState.SUCCESSFUL_REGISTRATION, student -> student.setGroup(group));
    }

    public void updateStudentNotification(Long studentId, Boolean notificationStatus) {
        Notification notification = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId))
                .getNotification();

        notification.setEnabled(notificationStatus);
        notificationRepository.save(notification);

        proceedRegistrationState(studentId, notification, RegistrationState.SUCCESSFUL_REGISTRATION,
                RegistrationState.SUCCESSFUL_REGISTRATION, student -> student.setNotification(notification));
    }

    public void updateStudentRegistrationState(Long studentId, RegistrationState state) {
        Student student = this.findStudentById(studentId);
        student.setRegistrationState(state);
        log.debug("Student {} updated state to {}", studentId, state);
        studentRepository.save(student);
    }

    @Transactional
    public Long createStudentIfNotExists(Long studentId, Long chatId) {
        Student student = studentRepository.findById(studentId)
                .orElseGet(() -> createStudent(studentId, chatId));
        return student.getUserId();
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

    private void proceedRegistrationState(Long studentId, Object newValue, RegistrationState validState,
                                          RegistrationState newState, Consumer<Student> setter) {
        Student student = this.findStudentById(studentId);
        validateRegistrationState(student, newValue, validState);

        student.setRegistrationState(newState);
        setter.accept(student);

        studentRepository.save(student);
        log.debug("Student {} updated state to {} with value {}", studentId, newState, newValue);
    }

    @Transactional
    private Student createStudent(Long studentId, Long chatId) {
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
}
