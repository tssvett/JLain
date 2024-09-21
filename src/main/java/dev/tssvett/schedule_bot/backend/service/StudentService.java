package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.entity.Student;
import dev.tssvett.schedule_bot.backend.entity.Faculty;
import dev.tssvett.schedule_bot.backend.entity.Group;
import dev.tssvett.schedule_bot.backend.entity.Notification;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.repository.NotificationRepository;
import dev.tssvett.schedule_bot.backend.repository.StudentRepository;
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
    public Student findUserById(Long userId) {
        return studentRepository.findById(userId).orElseThrow(() -> new StudentNotExistsException("No user with id: " + userId));
    }

    public Student chooseFaculty(Long userId, Faculty faculty) {
        Student student = studentRepository.findById(userId).orElseThrow(() -> new StudentNotExistsException("No user with id: " + userId));
        if (!student.getRegistrationState().equals(FACULTY_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s faculty with wrong state %s",
                    faculty.getName(), student.getRegistrationState()));
        } else {
            log.info("User {} choose faculty {}. Save it to database", userId, faculty.getName());
            student.setFaculty(faculty);
            student.setRegistrationState(RegistrationState.COURSE_CHOOSING);

            return studentRepository.save(student);
        }
    }

    public Student chooseCourse(Long userId, Long course) {
        Student student = studentRepository.findById(userId).orElseThrow(() -> new StudentNotExistsException("No user with id: " + userId));
        if (!student.getRegistrationState().equals(RegistrationState.COURSE_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s course with wrong state %s",
                    course, student.getRegistrationState()));
        } else {
            log.info("User {} choose course {}. Save {} course into database", userId, course, course);
            student.setCourse(course);
            student.setRegistrationState(RegistrationState.GROUP_CHOOSING);

            return studentRepository.save(student);
        }
    }

    public Student chooseGroup(Long userId, Group group) {
        Student student = studentRepository.findById(userId).orElseThrow(() -> new StudentNotExistsException("No user with id: " + userId));
        if (!student.getRegistrationState().equals(RegistrationState.GROUP_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s group with wrong state %s",
                    group.getName(), student.getRegistrationState()));
        } else {
            log.info("User {} choose group {}. Save it to database", userId, group.getName());
            student.setGroup(group);
            student.setRegistrationState(SUCCESSFUL_REGISTRATION);

            return studentRepository.save(student);
        }
    }

    public Student chooseNotification(Long userId, boolean notificationStatus) {
        return studentRepository.findById(userId)
                .map(user -> {
                    Notification notification = user.getNotification();
                    notification.setEnabled(notificationStatus);
                    notificationRepository.save(notification);
                    return studentRepository.save(user);
                })
                .orElseThrow(() -> new StudentNotExistsException("User not found with ID: " + userId));
    }

    public boolean chooseReRegistration(Long userId, String answer) {
        Student student = studentRepository.findById(userId).orElseThrow(() -> new StudentNotExistsException("No user with id: " + userId));
        if (!student.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s re-registration with wrong state %s",
                    answer, student.getRegistrationState()));
        } else {
            log.info("User {} choose answer {} to re-registration.", userId, answer);
            if (answer.equals(YES)) {
                student.setRegistrationState(FACULTY_CHOOSING);
                studentRepository.save(student);
                return true;
            } else {
                return false;
            }
        }
    }

    public Student createUserIfNotExists(Long userId, Long chatId) {
        return studentRepository.findById(userId).orElseGet(() -> {
            log.info("User {} is not in database. Add them to database", userId);
            Notification notification = Notification.builder()
                    .enabled(true)
                    .build();

            Student newUser = Student.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .registrationState(START_REGISTER)
                    .notification(notification)
                    .build();

            notification.setStudent(newUser);
            studentRepository.save(newUser);

            return newUser;
        });
    }

    public void changeUserRegistrationState(Student student, RegistrationState state) {
        student.setRegistrationState(state);
        Student savedUser = studentRepository.save(student);
        log.info("User {} registration state changed to {}", savedUser.getUserId(), savedUser.getRegistrationState());
    }

    public Boolean isExist(Long userId) {
        return studentRepository.findById(userId).isPresent();
    }

    public Boolean isRegistered(Long userId) {
        return studentRepository.findById(userId).map(botUser -> botUser.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)).orElse(false);
    }
}
