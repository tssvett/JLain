package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.FACULTY_CHOOSING;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.SUCCESSFUL_REGISTRATION;
import static dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants.YES;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
    }

    public Student chooseFaculty(Long studentId, Faculty faculty) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
        if (!student.getRegistrationState().equals(FACULTY_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("Student click on %s faculty with wrong state %s",
                    faculty.getName(), student.getRegistrationState()));
        } else {
            log.info("Student {} choose faculty {}. Save it to database", studentId, faculty.getName());
            student.setFaculty(faculty);
            student.setRegistrationState(RegistrationState.COURSE_CHOOSING);

            return studentRepository.save(student);
        }
    }

    public Student chooseCourse(Long studentId, Long course) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
        if (!student.getRegistrationState().equals(RegistrationState.COURSE_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("Student click on %s course with wrong state %s",
                    course, student.getRegistrationState()));
        } else {
            log.info("Student {} choose course {}. Save {} course into database", studentId, course, course);
            student.setCourse(course);
            student.setRegistrationState(RegistrationState.GROUP_CHOOSING);

            return studentRepository.save(student);
        }
    }

    public Student chooseGroup(Long studentId, Group group) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
        if (!student.getRegistrationState().equals(RegistrationState.GROUP_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("Student click on %s group with wrong state %s",
                    group.getName(), student.getRegistrationState()));
        } else {
            log.info("Student {} choose group {}. Save it to database", studentId, group.getName());
            student.setGroup(group);
            student.setRegistrationState(SUCCESSFUL_REGISTRATION);

            return studentRepository.save(student);
        }
    }

    public Student chooseNotification(Long studentId, boolean notificationStatus) {
        return studentRepository.findById(studentId)
                .map(user -> {
                    Notification notification = user.getNotification();
                    notification.setEnabled(notificationStatus);
                    notificationRepository.save(notification);
                    return studentRepository.save(user);
                })
                .orElseThrow(() -> new StudentNotExistsException("Student not found with ID: " + studentId));
    }

    public boolean chooseReRegistration(Long studentId, String answer) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
        if (!student.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)) {
            throw new NotValidRegistrationStateException(String.format("Student click on %s re-registration with wrong state %s",
                    answer, student.getRegistrationState()));
        } else {
            log.info("Student {} choose answer {} to re-registration.", studentId, answer);
            if (answer.equals(YES)) {
                student.setRegistrationState(FACULTY_CHOOSING);
                studentRepository.save(student);
                return true;
            } else {
                return false;
            }
        }
    }

    public Student createStudentIfNotExists(Long studentId, Long chatId) {
        return studentRepository.findById(studentId).orElseGet(() -> {
            log.info("Student {} is not in database. Add them to database", studentId);
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
            studentRepository.save(newStudent);

            return newStudent;
        });
    }

    public void changeStudentRegistrationState(Student student, RegistrationState state) {
        student.setRegistrationState(state);
        Student savedUser = studentRepository.save(student);
        log.info("Student {} registration state changed to {}", savedUser.getUserId(), savedUser.getRegistrationState());
    }

    public Boolean isExist(Long studentId) {
        return studentRepository.findById(studentId).isPresent();
    }

    public Boolean isRegistered(Long studentId) {
        return studentRepository.findById(studentId).map(student -> student.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)).orElse(false);
    }
}
