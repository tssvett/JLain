package dev.tssvett.schedule_bot.bot.command.impl.register;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import static dev.tssvett.schedule_bot.bot.enums.keyboard.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.bot.enums.keyboard.Action.REFRESH_REGISTRATION;
import static dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState.FACULTY_CHOOSING;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.keyboard.impl.refresh.RefreshRegistrationKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterBotCommand implements BotCommand {
    private final StudentService studentService;
    private final FacultyService facultyService;
    private final RefreshRegistrationKeyboard refreshRegistrationKeyboard;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    @DirectMessageRequired
    public SendMessage execute(Long userId, Long chatId, String message) {
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
        }

        if (facultyService.findAllFaculties().isEmpty()) {
            log.info("No faculties found.");

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageTextConstantsUtils.NO_FACULTIES_FOUND_MESSAGE)
                    .build();
        }

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
