package dev.tssvett.schedule_bot.bot.keyboard.impl.course;

import dev.tssvett.schedule_bot.backend.entity.Student;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.group.GroupKeyboard;
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
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long course = Long.parseLong(callbackDetails.getCallbackInformation());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return createCourseChooseMessage(userId, chatId, course);
    }

    public SendMessage createCourseChooseMessage(Long userId, Long chatId, Long course) {
        try {
            Student user = studentService.chooseCourse(userId, course);
            log.info("User {} successfully choose course {}", user.getUserId(), course);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                    .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE, userId))
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose course {} but it's already chosen", userId, course);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.COURSE_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
