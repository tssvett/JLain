package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.actions.keyboard.impl.CourseKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.FacultyKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.GroupKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.ReRegistrateKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.exception.UserNotExistsException;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.schedule.group.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.enums.Action.COURSE_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.REREGISTRATE;
import static dev.tssvett.schedule_bot.enums.RegistrationState.FACULTY_CHOOSING;
import static dev.tssvett.schedule_bot.enums.RegistrationState.START_REGISTER;
import static dev.tssvett.schedule_bot.enums.RegistrationState.SUCCESSFUL_REGISTRATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final ReRegistrateKeyboard reRegistrateKeyboard;
    private final FacultyKeyboard facultyKeyboard;
    private final CourseKeyboard courseKeyboard;
    private final GroupKeyboard groupKeyboard;

    public SendMessage registerUserCommandCallback(Long userId, Long chatId) {
        BotUser botUser = userRepository.findById(userId).orElseGet(() -> {
            log.info("User {} is not in database. Add them to database and continue registration process", userId);
            BotUser newUser = BotUser.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .registrationState(START_REGISTER)
                    .build();
            userRepository.save(newUser);
            return newUser;
        });

        if (isSuccessfullyRegistered(botUser)) {
            log.info("User {} is successfully registered. Asking for re-registration.", userId);
            return reRegistrationSendMessage(chatId);
        } else {
            log.info("User {} is not registered with SUCCESSFUL_REGISTRATION. Continuing registration process.", userId);
            botUser.setRegistrationState(FACULTY_CHOOSING);
            userRepository.save(botUser);
            return chooseFacultySendMessage(userId, chatId);
        }
    }

    public SendMessage chooseFacultyStepCallback(Long userId, Long chatId, Faculty faculty) {

        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));

        try {
            if (!botUser.getRegistrationState().equals(RegistrationState.FACULTY_CHOOSING)) {
                throw new NotValidRegistrationStateException(String.format("User click on %s faculty with wrong state %s",
                        faculty.getName(), botUser.getRegistrationState()));
            } else {
                log.info("User {} choose faculty {}. Save {} into database", userId, faculty.getName(), faculty.getName());
                botUser.setFacultyName(faculty.getName());
                botUser.setRegistrationState(RegistrationState.COURSE_CHOOSING);
                userRepository.save(botUser);
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(MessageConstants.REGISTER_CHOOSE_COURSE_MESSAGE)
                        .replyMarkup(courseKeyboard.createInlineKeyboard(COURSE_CHOOSE))
                        .build();
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn(e.getMessage());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы пытаетесь нажать на кнопку выбора факультета будучи в другом состоянии регистрации. Будьте внимательнее гады.")
                    .build();
        }
    }

    public SendMessage chooseCourseStepCallback(Long userId, Long chatId, Integer courseNumber) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));

        try {
            if (!botUser.getRegistrationState().equals(RegistrationState.COURSE_CHOOSING)) {
                throw new NotValidRegistrationStateException(String.format("User click on %s course with wrong state %s",
                        courseNumber, botUser.getRegistrationState()));
            } else {
                log.info("User {} choose course {}. Save {} course into database", userId, courseNumber, courseNumber);
                botUser.setCourse(courseNumber.toString());
                botUser.setRegistrationState(RegistrationState.GROUP_CHOOSING);
                userRepository.save(botUser);
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                        .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE))
                        .build();
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn(e.getMessage());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы пытаетесь нажать на кнопку выбора курса будучи в другом состоянии регистрации. Будьте внимательнее гады.")
                    .build();
        }
    }

    public SendMessage chooseGroupStepCallback(Long userId, Long chatId, Group group) {

        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));

        try {
            if (!botUser.getRegistrationState().equals(RegistrationState.GROUP_CHOOSING)) {
                throw new NotValidRegistrationStateException(String.format("User click on %s group with wrong state %s",
                        group.getName(), botUser.getRegistrationState()));
            } else {
                log.info("User {} choose group {}. Save {} into database", userId, group.getName(), group.getName());
                botUser.setGroupName(group.getName());
                botUser.setRegistrationState(SUCCESSFUL_REGISTRATION);
                userRepository.save(botUser);
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE)
                        .build();
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn(e.getMessage());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы пытаетесь нажать на кнопку выбора группы будучи в другом состоянии регистрации. Будьте внимательнее гады.")
                    .build();
        }
    }

    public SendMessage reRegistrationStepCallback(Long userId, Long chatId, String answer) {

        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));

        try {
            if (!botUser.getRegistrationState().equals(SUCCESSFUL_REGISTRATION)) {
                throw new NotValidRegistrationStateException(String.format("User click on %s re-registration with wrong state %s",
                        answer, botUser.getRegistrationState()));
            } else {
                log.info("User {} choose answer {} to re-registration.", userId, answer);
                if (answer.equals("Да")) {
                    botUser.setRegistrationState(FACULTY_CHOOSING);
                    userRepository.save(botUser);
                    return SendMessage.builder()
                            .chatId(chatId)
                            .text(MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE)
                            .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE))
                            .build();
                } else {
                    return SendMessage.builder()
                            .chatId(chatId)
                            .text("Ну нет так нет...")
                            .build();
                }
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn(e.getMessage());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы пытаетесь нажать на кнопку выбора ре-регистрации будучи в другом состоянии регистрации. Будьте внимательнее гады.")
                    .build();
        }
    }

    private SendMessage reRegistrationSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(reRegistrateKeyboard.createInlineKeyboard(REREGISTRATE))
                .text(MessageConstants.ALREADY_REGISTERED_MESSAGE)
                .build();
    }

    private SendMessage chooseFacultySendMessage(Long userId, Long chatId) {
        changeUserRegistrationState(userId, FACULTY_CHOOSING);
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE))
                .text(MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE)
                .build();
    }

    private void changeUserRegistrationState(Long userId, RegistrationState state) {
        log.info("User {} registration state changed to {}", userId, state);
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("No user with id: " + userId));
        botUser.setRegistrationState(state);
        userRepository.save(botUser);
    }

    private boolean isSuccessfullyRegistered(BotUser botUser) {

        log.info("Current registration state: {} ", botUser.getRegistrationState().toString());
        return botUser.getRegistrationState().equals(SUCCESSFUL_REGISTRATION);
    }
}
