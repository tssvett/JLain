package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.course.CourseKeyboard;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.Action.COURSE_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultySelectionKeyboardButton implements KeyboardButton {
    private final StudentService studentService;
    private final CourseKeyboard courseKeyboard;

    @Override
    public SendMessage onButtonClick(Update update) {
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        long facultyId = UpdateUtils.getFacultyId(update);

        return processFacultySelectionOnButtonClick(userId, chatId, facultyId);
    }

    private SendMessage processFacultySelectionOnButtonClick(long userId, long chatId, long facultyId) {
        try {
            studentService.updateStudentFaculty(userId, facultyId);

            return sendCourseSelectionMessage(userId, chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose faculty {} but it's already chosen", userId, facultyId);

            return sendAlreadySelectedFacultyMessage(chatId);
        }
    }

    private SendMessage sendCourseSelectionMessage(long userId, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.REGISTER_CHOOSE_COURSE_MESSAGE)
                .replyMarkup(courseKeyboard.createInlineKeyboard(COURSE_CHOOSE, userId))
                .build();
    }

    private SendMessage sendAlreadySelectedFacultyMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.FACULTY_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
