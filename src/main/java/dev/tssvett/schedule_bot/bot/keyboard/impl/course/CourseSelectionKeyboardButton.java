package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.group.GroupKeyboard;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseSelectionKeyboardButton implements KeyboardButton {
    private final StudentService studentService;
    private final GroupKeyboard groupKeyboard;

    @Override
    public SendMessage onButtonClick(Update update) {
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        long course = UpdateUtils.getCourse(update);

        return processCourseSelectionOnButtonClick(userId, chatId, course);
    }


    private SendMessage processCourseSelectionOnButtonClick(long userId, long chatId, long course) {
        try {
            studentService.updateStudentCourse(userId, course);

            return sendGroupSelectionMessage(userId, chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose course {} but it's already chosen", userId, course);

            return sendAlreadySelectedCourseMessage(chatId);
        }
    }

    private SendMessage sendGroupSelectionMessage(long userId, long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE, userId))
                .build();
    }

    private SendMessage sendAlreadySelectedCourseMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.COURSE_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
