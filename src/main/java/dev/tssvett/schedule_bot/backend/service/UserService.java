package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.entity.Faculty;
import dev.tssvett.schedule_bot.backend.entity.Group;
import dev.tssvett.schedule_bot.backend.entity.Notification;
import dev.tssvett.schedule_bot.backend.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.exception.UserNotExistsException;
import dev.tssvett.schedule_bot.backend.repository.FacultyRepository;
import dev.tssvett.schedule_bot.backend.repository.GroupRepository;
import dev.tssvett.schedule_bot.backend.repository.NotificationRepository;
import dev.tssvett.schedule_bot.backend.repository.UserRepository;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.tssvett.schedule_bot.bot.constants.MessageConstants.YES;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.FACULTY_CHOOSING;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.START_REGISTER;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.SUCCESSFUL_REGISTRATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FacultyRepository facultyRepository;
    private final NotificationRepository notificationRepository;

    public BotUser findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
    }

    public BotUser chooseFaculty(Long userId, Faculty faculty) {
        BotUser botUser = this.findUserById(userId);
        if (!botUser.getRegistrationState().equals(FACULTY_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s faculty with wrong state %s",
                    faculty.getName(), botUser.getRegistrationState()));
        } else {
            log.info("User {} choose faculty {}. Save it to database", userId, faculty.getName());
            botUser.setFaculty(faculty);
            botUser.setRegistrationState(RegistrationState.COURSE_CHOOSING);

            return userRepository.save(botUser);
        }
    }

    public BotUser chooseCourse(Long userId, Long course) {
        BotUser botUser = this.findUserById(userId);
        if (!botUser.getRegistrationState().equals(RegistrationState.COURSE_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s course with wrong state %s",
                    course, botUser.getRegistrationState()));
        } else {
            log.info("User {} choose course {}. Save {} course into database", userId, course, course);
            botUser.setCourse(course);
            botUser.setRegistrationState(RegistrationState.GROUP_CHOOSING);

            return userRepository.save(botUser);
        }
    }

    public BotUser chooseGroup(Long userId, Group group) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.GROUP_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s group with wrong state %s",
                    group.getName(), botUser.getRegistrationState()));
        } else {
            log.info("User {} choose group {}. Save it to database", userId, group.getName());
            botUser.setGroup(group);
            botUser.setRegistrationState(SUCCESSFUL_REGISTRATION);

            return userRepository.save(botUser);
        }
    }

    public BotUser chooseNotification(Long userId, boolean notificationStatus) {
        return userRepository.findById(userId)
                .map(user -> {
                    Notification notification = user.getNotification();
                    notification.setEnabled(notificationStatus);
                    notificationRepository.save(notification);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotExistsException("User not found with ID: " + userId));
    }

    public boolean chooseReRegistration(Long userId, String answer) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s re-registration with wrong state %s",
                    answer, botUser.getRegistrationState()));
        } else {
            log.info("User {} choose answer {} to re-registration.", userId, answer);
            if (answer.equals(YES)) {
                botUser.setRegistrationState(FACULTY_CHOOSING);
                userRepository.save(botUser);
                return true;
            } else {
                return false;
            }
        }
    }

    public BotUser createUserIfNotExists(Long userId, Long chatId) {
        return userRepository.findById(userId).orElseGet(() -> {
            log.info("User {} is not in database. Add them to database", userId);
            Notification notification = Notification.builder()
                    .enabled(true)
                    .build();

            BotUser newUser = BotUser.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .registrationState(START_REGISTER)
                    .notification(notification)
                    .build();

            notification.setBotUser(newUser);
            userRepository.save(newUser);

            return newUser;
        });
    }

    public BotUser changeUserRegistrationState(Long userId, RegistrationState state) {
        BotUser botUser = this.findUserById(userId);
        botUser.setRegistrationState(state);
        BotUser savedUser = userRepository.save(botUser);
        log.info("User {} registration state changed to {}", savedUser.getUserId(), savedUser.getRegistrationState());
        return savedUser;
    }
}
