package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;


import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.group.Group;
import dev.tssvett.schedule_bot.schedule.parser.GroupParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboardCallback implements KeyboardCallback {
    private final GroupParser groupParser;
    private final UserRepository userRepository;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Group group = findGroupById(callbackDetails.getCallbackText());
        try {
            saveUserToDatabase(userId, group);
        }
        catch (NotValidRegistrationStateException e){
            log.warn(e.getMessage());
            sendMessage.setText("Вы уже выбрали группу, кайфуте");
            return sendMessage;
        }
        sendMessage.setText(MessageConstants.SUCCESSFULLY_REGISTERED_MESSAGE);
        return sendMessage;
    }

    private Group findGroupById(String id) {
        List<Group> groupList = groupParser.parse();
        for (Group group : groupList) {
            if (id.equals(group.getId())) {
                return group;
            }
        }
        return null;
    }

    private void saveUserToDatabase(Long userId, Group group) {
        log.info("Saving user to database with userId: {} and groupName: {}", userId, group.getName());
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.COURSE_CHOOSED)){
            throw new NotValidRegistrationStateException("User clicked on already chosen group");
        }
        botUser.setGroupName(group.getName());
        botUser.setRegistrationState(RegistrationState.GROUP_CHOOSED);
        botUser.setRegistrationState(RegistrationState.SUCCESS);
        userRepository.save(botUser);
    }
}

