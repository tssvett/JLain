package dev.tssvett.schedule_bot.bot.keyboard.impl.reregister;

import dev.tssvett.schedule_bot.backend.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.faculty.FacultyKeyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.Action.FACULTY_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReRegistrateButton implements KeyboardButton {
    private final UserService userService;
    private final FacultyKeyboard facultyKeyboard;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();
        String answer = callbackDetails.getCallbackInformation();

        return chooseReRegistrationSendMessage(userId, chatId, answer);
    }

    public SendMessage chooseReRegistrationSendMessage(Long userId, Long chatId, String answer) {
        try {
            if (userService.chooseReRegistration(userId, answer)) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(MessageConstants.REGISTER_FACULTY_CHOOSING_MESSAGE)
                        .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                        .build();
            } else {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(MessageConstants.NO_RE_REGISTRATION_ANSWER)
                        .build();
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose ReRegistration but it's wrong state", userId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.REGISTRATION_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
