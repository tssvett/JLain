package dev.tssvett.schedule_bot.actions.keyboard.impl.course;

import dev.tssvett.schedule_bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.impl.group.GroupKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.constants.MessageConstants.COURSE_CLICK_WITH_ERROR_STATE;
import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardButton implements KeyboardButton {
    private final UserService userService;
    private final GroupKeyboard groupKeyboard;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Integer courseNumber = Integer.parseInt(callbackDetails.getCallbackInformation());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return createCourseChooseMessage(userId, chatId, courseNumber);
    }

    public SendMessage createCourseChooseMessage(Long userId, Long chatId, Integer courseNumber) {
        try {
            BotUser userWithChosenCourse = userService.chooseCourse(userId, courseNumber.toString());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE)
                    .replyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE, userId))
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose course {} but it's already chosen", userId, courseNumber);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(COURSE_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
