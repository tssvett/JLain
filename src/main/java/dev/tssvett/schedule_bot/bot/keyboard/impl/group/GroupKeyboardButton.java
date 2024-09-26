package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboardButton implements KeyboardButton {
    private final GroupService groupService;
    private final StudentService studentService;

    @Override
    public SendMessage click(Update update) {
        Long groupId = Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Group group = groupService.findGroupById(groupId);

        return chooseGroupSendMessage(userId, chatId, group);
    }

    public SendMessage chooseGroupSendMessage(Long userId, Long chatId, Group group) {
        try {
            studentService.updateStudentGroup(userId, group);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE)
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose group {} but it's already chosen", userId, group.getName());

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.GROUP_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
