package dev.tssvett.schedule_bot.bot.actions.command.impl.register;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.keyboard.impl.refresh.RefreshRegistrationKeyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dev.tssvett.schedule_bot.bot.enums.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.bot.enums.Action.REFRESH_REGISTRATION;
import static dev.tssvett.schedule_bot.bot.enums.RegistrationState.FACULTY_CHOOSING;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterBotCommand implements BotCommand {
    private final StudentService studentService;
    private final RefreshRegistrationKeyboard refreshRegistrationKeyboard;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    @DirectMessageRequired
    public SendMessage execute(Long userId, Long chatId) {
        return sendRegisterCommandMessage(userId, chatId);
    }

    private SendMessage sendRegisterCommandMessage(Long userId, Long chatId) {
        if (studentService.isRegistered(userId)) {
            log.info("User {} is successfully registered. Asking for re-registration.", userId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(refreshRegistrationKeyboard.createInlineKeyboard(REFRESH_REGISTRATION, userId))
                    .text(MessageTextConstantsUtils.ALREADY_REGISTERED_MESSAGE)
                    .build();
        } else {
            log.info("User {} is not registered with SUCCESSFUL_REGISTRATION. Starting registration process.", userId);
            studentService.createStudentIfNotExists(userId, chatId);
            studentService.updateStudentRegistrationState(userId, FACULTY_CHOOSING);

            return SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                    .text(MessageTextConstantsUtils.REGISTER_FACULTY_CHOOSING_MESSAGE)
                    .build();
        }
    }
}
