package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.NotificationNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;


    public StudentRecord getStudentInfoById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + studentId));
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

        notificationRepository.updateTomorrowScheduleStatus(notificationId, notificationStatus);

        proceedRegistrationState(studentId, RegistrationState.SUCCESSFUL_REGISTRATION,
                RegistrationState.SUCCESSFUL_REGISTRATION, student -> student.setNotificationId(notificationId));
    }

    public void updateStudentRegistrationState(Long studentId, RegistrationState state) {
        log.debug("Student {} updated state to {}", studentId, state);
        studentRepository.updateState(studentId, state.name());
    }

    public void createStudentIfNotExists(Long studentId, Long chatId) {
        if (studentRepository.findById(studentId).isEmpty()) {
            createStudent(studentId, chatId);
        }
    }


    public boolean isRegistered(Long studentId) {
        Optional<StudentRecord> student = studentRepository.findById(studentId);

        return student.isPresent() && student.get().getRegistrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION.toString());
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

        studentRepository.updateAllFields(student.getUserId(), student);
        log.debug("Student {} updated state to {}", studentId, newState);
    }

    private void createStudent(Long studentId, Long chatId) {
        log.debug("Student {} is not in database. Adding them to database", studentId);

        StudentRecord newStudent = new StudentRecord(
                studentId,
                chatId,
                null,
                START_REGISTER.name(),
                null,
                null,
                null
        );

        studentRepository.save(newStudent);

        NotificationRecord notification = new NotificationRecord(null, true, studentId, true);
        NotificationRecord savedNotification = notificationRepository.save(notification);

        studentRepository.updateNotificationId(newStudent, savedNotification.getId());

    }

    public boolean isTomorrowScheduleNotificationEnabled(Long userId) {
        StudentRecord studentRecord = this.getStudentInfoById(userId);

        return notificationRepository.findById(studentRecord.getNotificationId())
                .orElseThrow(() -> new NotificationNotExistsException("No notification with id: " + studentRecord.getNotificationId()))
                .getTomorrowScheduleEnabled();
    }

    public boolean isScheduleDifferenceNotificationEnabled(Long userId) {
        StudentRecord studentRecord = this.getStudentInfoById(userId);

        return notificationRepository.findById(studentRecord.getNotificationId())
                .orElseThrow(() -> new NotificationNotExistsException("No notification with id: " + studentRecord.getNotificationId()))
                .getScheduleDifferenceEnabled();
    }
}
