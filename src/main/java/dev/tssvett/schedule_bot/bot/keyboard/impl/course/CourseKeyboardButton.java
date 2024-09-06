package dev.tssvett.schedule_bot.bot.actions.keyboard.impl.course;

import dev.tssvett.schedule_bot.backend.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.group.GroupKeyboard;
import dev.tssvett.schedule_bot.bot.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.constants.MessageConstants.COURSE_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardButton implements KeyboardButton {
    private final UserService userService;
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
            userService.chooseCourse(userId, course);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                    .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE, userId))
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose course {} but it's already chosen", userId, course);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(COURSE_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
