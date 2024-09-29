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
public class GroupKeyboardButton implements KeyboardButton {
    private final StudentService studentService;

    @Override
    public SendMessage click(Update update) {
        Long userId = UpdateUtils.getUserId(update);
        Long chatId = UpdateUtils.getChatId(update);
        Long groupId = UpdateUtils.getGroupId(update);

        return handleClick(userId, chatId, groupId);
    }

    private SendMessage handleClick(Long userId, Long chatId, Long groupId) {
        try {
            studentService.updateStudentGroup(userId, groupId);

            return chooseGroupSendMessage(chatId);

        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose group {} but it's already chosen", userId, groupId);

            return chooseGroupWrongStateSendMessage(chatId);
        }
    }

    private SendMessage chooseGroupSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE)
                .build();
    }

    private SendMessage chooseGroupWrongStateSendMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.GROUP_CLICK_WITH_ERROR_STATE)
                .build();
    }
}
