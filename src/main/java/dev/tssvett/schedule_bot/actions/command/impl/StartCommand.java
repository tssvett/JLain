package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.enums.RegistrationState.SUCCESS;

@Slf4j
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final UserRepository userRepository;
    @Override
    public SendMessage execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        log.info("Received " + this.getClass().getSimpleName() +  " from userId: {}", userId);
        saveUserToDatabase(userId, chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(MessageConstants.START_COMMAND);
        return sendMessage;
    }

    private void saveUserToDatabase(Long userId, Long chatId){
        if (userRepository.findById(userId).isPresent()){
            return;
        }
        log.info("Saving to database new user with chatId: {} and userId: {}", chatId, userId);
        BotUser botUser = BotUser.builder()
                .userId(userId)
                .chatId(chatId)
                .registrationState(RegistrationState.START)
                .build();
        userRepository.save(botUser);
    }
}
