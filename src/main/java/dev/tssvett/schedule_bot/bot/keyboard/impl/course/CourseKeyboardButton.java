package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
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
public class CourseKeyboardButton implements KeyboardButton {
    private final StudentService studentService;
    private final GroupKeyboard groupKeyboard;

    @Override
    public SendMessage click(Update update) {
        Long chatId = UpdateUtils.getChatId(update);
        Long userId = UpdateUtils.getUserId(update);
        Long course = UpdateUtils.getCourse(update);

        return handleClick(userId, chatId, course);
    }


    private SendMessage handleClick(Long userId, Long chatId, Long course) {
        try {
            studentService.updateStudentCourse(userId, course);

            return chooseGroupSendMessage(userId, chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose course {} but it's already chosen", userId, course);

            return chooseGroupWrongStateSendMessage(chatId);
        }
    }

    private SendMessage chooseGroupSendMessage(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE, userId))
                .build();
    }

    private SendMessage chooseGroupWrongStateSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.COURSE_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
