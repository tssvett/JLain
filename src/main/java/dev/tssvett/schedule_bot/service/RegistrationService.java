package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.actions.keyboard.impl.course.CourseKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.group.GroupKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.reregister.ReRegistrateKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.entity.Notification;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.exception.UserNotExistsException;
import dev.tssvett.schedule_bot.repository.NotificationRepository;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.entity.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.constants.MessageConstants.COURSE_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.constants.MessageConstants.FACULTY_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.constants.MessageConstants.GROUP_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.constants.MessageConstants.NO_RE_REGISTRATION_ANSWER;
import static dev.tssvett.schedule_bot.constants.MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE;
import static dev.tssvett.schedule_bot.constants.MessageConstants.REGISTRATION_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.constants.MessageConstants.YES;
import static dev.tssvett.schedule_bot.enums.Action.COURSE_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.REREGISTRATE;
import static dev.tssvett.schedule_bot.enums.RegistrationState.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ReRegistrateKeyboard reRegistrateKeyboard;
    private final FacultyKeyboard facultyKeyboard;
    private final GroupKeyboard groupKeyboard;

    @Transactional
    public SendMessage registerUserCommandCallback(Long userId, Long chatId) {
        BotUser botUser = userRepository.findById(userId).orElseGet(() -> {
            log.info("User {} is not in database. Add them to database and continue registration process", userId);
            Notification notification = Notification.builder()
                    .enabled(true)
                    .build();

            BotUser newUser = BotUser.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .registrationState(START_REGISTER)
                    .notification(notification)
                    .build();


            //устанавливаем связи
            notification.setBotUser(newUser);

            userRepository.save(newUser);
            notificationRepository.save(notification);

            return newUser;
        });

        if (isSuccessfullyRegistered(botUser)) {
            log.info("User {} is successfully registered. Asking for re-registration.", userId);

            return reRegistrationSendMessage(chatId, userId);
        } else {
            log.info("User {} is not registered with SUCCESSFUL_REGISTRATION. Starting registration process.", userId);
            botUser.setRegistrationState(FACULTY_CHOOSING);
            userRepository.save(botUser);

            return chooseFacultySendMessage(userId, chatId);
        }
    }

    @Transactional
    public void startCommandCallback(Long userId, Long chatId) {
        BotUser botUser = userRepository.findById(userId).orElseGet(() -> {
            log.info("User {} is a new user!. Add them to database", userId);

            // Создаем нового пользователя
            BotUser newUser = BotUser.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .registrationState(START_REGISTER)
                    .build();

            // Сохраняем newUser
            userRepository.save(newUser);

            // Создаем новый объект Notification и устанавливаем связь
            Notification notification = Notification.builder()
                    .enabled(true)
                    .botUser(newUser) // Устанавливаем связь с newUser
                    .build();

            // Сохраняем notification
            notificationRepository.save(notification);

            return newUser;
        });
        log.info("User {} registration state: {}", userId, botUser.getRegistrationState());
    }


    private SendMessage reRegistrationSendMessage(Long chatId, Long userId) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(reRegistrateKeyboard.createInlineKeyboard(REREGISTRATE, userId))
                .text(MessageConstants.ALREADY_REGISTERED_MESSAGE)
                .build();
    }

    private SendMessage chooseFacultySendMessage(Long userId, Long chatId) {
        changeUserRegistrationState(userId, FACULTY_CHOOSING);

        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                .text(REGISTER_FACULTY_CHOOSING_MESSAGE)
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
