package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.entity.Notification;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.exception.UserNotExistsException;
import dev.tssvett.schedule_bot.repository.NotificationRepository;
import dev.tssvett.schedule_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.constants.MessageConstants.NO_RE_REGISTRATION_ANSWER;
import static dev.tssvett.schedule_bot.constants.MessageConstants.YES;
import static dev.tssvett.schedule_bot.enums.RegistrationState.FACULTY_CHOOSING;
import static dev.tssvett.schedule_bot.enums.RegistrationState.SUCCESSFUL_REGISTRATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    public BotUser findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
    }

    public BotUser chooseFaculty(Long userId, String facultyName) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.FACULTY_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s faculty with wrong state %s",
                    facultyName, botUser.getRegistrationState()));
        } else {
            log.info("User {} choose faculty {}. Save {} into database", userId, facultyName, facultyName);
            botUser.setFacultyName(facultyName);
            botUser.setRegistrationState(RegistrationState.COURSE_CHOOSING);

            return userRepository.save(botUser);
        }
    }

    public BotUser chooseCourse(Long userId, String course) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
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

    public BotUser chooseGroup(Long userId, String groupName) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.GROUP_CHOOSING)) {
            throw new NotValidRegistrationStateException(String.format("User click on %s group with wrong state %s",
                    groupName, botUser.getRegistrationState()));
        } else {
            log.info("User {} choose group {}. Save {} into database", userId, groupName, groupName);
            botUser.setGroupName(groupName);
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
}
