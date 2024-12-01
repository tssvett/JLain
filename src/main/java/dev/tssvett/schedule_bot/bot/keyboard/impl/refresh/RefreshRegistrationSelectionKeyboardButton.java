package dev.tssvett.schedule_bot.bot.keyboard.impl.refresh;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.keyboard.Action.FACULTY_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshRegistrationSelectionKeyboardButton implements KeyboardButton {
    private final StudentService studentService;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    public SendMessage onButtonClick(Update update) {
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        boolean isCorrectAnswer = UpdateUtils.getRefreshRegistrationStatus(update);

        return processRefreshRegistrationSelectionOnButtonClick(userId, chatId, isCorrectAnswer);
    }

    public SendMessage processRefreshRegistrationSelectionOnButtonClick(long userId, long chatId, boolean isCorrectAnswer) {
        if (!isCorrectAnswer) {
            return sendUserRejectRefreshRegistrationMessage(chatId);
        }
        studentService.updateStudentRegistrationState(userId, RegistrationState.FACULTY_CHOOSING);

        return sendFacultySelectionMessage(userId, chatId);
    }

    private SendMessage sendFacultySelectionMessage(long userId, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.REGISTER_FACULTY_CHOOSING_MESSAGE)
                .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                .build();
    }

    private SendMessage sendUserRejectRefreshRegistrationMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.NO_RE_REGISTRATION_ANSWER)
                .build();
    }
}
