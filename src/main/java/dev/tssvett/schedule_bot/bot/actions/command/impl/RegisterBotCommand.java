package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.reregister.ReRegistrateKeyboard;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.bot.constants.MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE;
import static dev.tssvett.schedule_bot.bot.enums.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.bot.enums.Action.REREGISTRATE;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.FACULTY_CHOOSING;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.SUCCESSFUL_REGISTRATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterBotCommand implements BotCommand {
    private final UserService userService;
    private final ReRegistrateKeyboard reRegistrateKeyboard;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    @DirectMessageRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received {} from userId: {}", this.getClass().getSimpleName(), userId);
        return sendRegisterCommandMessage(userId, chatId);
    }

    private SendMessage sendRegisterCommandMessage(Long userId, Long chatId) {
        BotUser user = userService.createUserIfNotExists(userId, chatId);
        if (isSuccessfullyRegistered(user)) {
            log.info("User {} is successfully registered. Asking for re-registration.", userId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(reRegistrateKeyboard.createInlineKeyboard(REREGISTRATE, userId))
                    .text(MessageConstants.ALREADY_REGISTERED_MESSAGE)
                    .build();
        } else {
            log.info("User {} is not registered with SUCCESSFUL_REGISTRATION. Starting registration process.", userId);
            userService.changeUserRegistrationState(userId, FACULTY_CHOOSING);
            return SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                    .text(REGISTER_FACULTY_CHOOSING_MESSAGE)
                    .build();
        }
    }

    private boolean isSuccessfullyRegistered(BotUser botUser) {
        log.info("Current registration state: {} ", botUser.getRegistrationState().toString());
        return botUser.getRegistrationState().equals(SUCCESSFUL_REGISTRATION);
    }
}
