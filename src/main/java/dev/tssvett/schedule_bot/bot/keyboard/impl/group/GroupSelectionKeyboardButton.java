package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@RequiredArgsConstructor
public class GroupSelectionKeyboardButton implements KeyboardButton {
    private final StudentService studentService;

    @Override
    public SendMessage onButtonClick(Update update) {
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        long groupId = UpdateUtils.getGroupId(update);

        return processGroupSelectionOnButtonClick(userId, chatId, groupId);
    }

    private SendMessage processGroupSelectionOnButtonClick(long userId, long chatId, long groupId) {
        try {
            studentService.updateStudentGroup(userId, groupId);

            return sendRegistrationCompletedMessage(chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose group {} but it's already chosen", userId, groupId);

            return sendAlreadySelectedGroupMessage(chatId);
        }
    }

    private SendMessage sendRegistrationCompletedMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE)
                .build();
    }

    private SendMessage sendAlreadySelectedGroupMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.GROUP_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
