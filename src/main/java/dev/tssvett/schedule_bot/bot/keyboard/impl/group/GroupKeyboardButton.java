package dev.tssvett.schedule_bot.bot.actions.keyboard.impl.group;


import dev.tssvett.schedule_bot.bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.entity.Group;
import dev.tssvett.schedule_bot.backend.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.constants.MessageConstants.GROUP_CLICK_WITH_ERROR_STATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboardButton implements KeyboardButton {
    private final GroupService groupService;
    private final UserService userService;

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
            BotUser userWithChosenGroup = userService.chooseGroup(userId, group);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE)
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose group {} but it's already chosen", userId, group.getName());
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(GROUP_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}