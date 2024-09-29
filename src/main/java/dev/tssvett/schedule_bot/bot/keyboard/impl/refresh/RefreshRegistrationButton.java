package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.Action.FACULTY_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshRegistrationButton implements KeyboardButton {
    private final StudentService studentService;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    public SendMessage click(Update update) {
        Long chatId = UpdateUtils.getChatId(update);
        Long userId = UpdateUtils.getUserId(update);
        Boolean isCorrectAnswer = UpdateUtils.getResreshRegistrationStatus(update);

        return handleClick(userId, chatId, isCorrectAnswer);
    }

    public SendMessage handleClick(Long userId, Long chatId, Boolean isCorrectAnswer) {
        try {
            if (!isCorrectAnswer) {
                return wrongAnswerRegisterSendMessage(chatId);
            }
            studentService.updateStudentRegistrationState(userId, RegistrationState.FACULTY_CHOOSING);

            return correctAnswerRegisterSendMessage(userId, chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose ReRegistration but it's wrong state", userId);

            return wrongStateRegisterSendMessage(chatId);
        }
    }

    private SendMessage correctAnswerRegisterSendMessage(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE)
                .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                .build();
    }

    private SendMessage wrongAnswerRegisterSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.NO_RE_REGISTRATION_ANSWER)
                .build();
    }

    private SendMessage wrongStateRegisterSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.REGISTRATION_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
