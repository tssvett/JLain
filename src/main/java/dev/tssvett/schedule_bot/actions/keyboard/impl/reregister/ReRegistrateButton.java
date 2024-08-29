package dev.tssvett.schedule_bot.actions.keyboard.impl.reregister;

import dev.tssvett.schedule_bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.impl.faculty.FacultyKeyboard;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.constants.MessageConstants.*;
import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;

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
                        .text(REGISTER_FACULTY_CHOOSING_MESSAGE)
                        .replyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE, userId))
                        .build();
            } else {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(NO_RE_REGISTRATION_ANSWER)
                        .build();
            }
        } catch (NotValidRegistrationStateException e) {
            log.warn(e.getMessage());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(REGISTRATION_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}