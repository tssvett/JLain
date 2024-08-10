package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.actions.keyboard.impl.FacultyKeyboard;
import dev.tssvett.schedule_bot.actions.keyboard.impl.ReRegistrateKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;
import static dev.tssvett.schedule_bot.enums.Action.REREGISTRATE;
import static dev.tssvett.schedule_bot.enums.RegistrationState.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterCommand implements Command {

    private final FacultyKeyboard facultyKeyboard;
    private final ReRegistrateKeyboard reRegistrateKerboard;
    private final UserRepository userRepository;
    @Override
    public SendMessage execute(Update update) {

        Long userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        log.info("Received " + this.getClass().getSimpleName()+  " from userId: {}", userId);
        SendMessage sendMessage = new SendMessage();
        changeState(userId, RegistrationState.START_REGISTER);
        if (checkAlreadyRegistered(userId)){
            sendMessage.setChatId(chatId);
            sendMessage.setText(MessageConstants.ALREADY_REGISTERED_MESSAGE);
            sendMessage.setReplyMarkup(reRegistrateKerboard.createInlineKeyboard(REREGISTRATE));
        }
        else {
            saveUserToDatabase(userId, chatId);
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(facultyKeyboard.createInlineKeyboard(FACULTY_CHOOSE));
            sendMessage.setText(MessageConstants.REGISTER_CHOOSE_FACULTY_MESSAGE);
        }
        return sendMessage;
    }

    private boolean checkAlreadyRegistered(Long userId){
        Optional<BotUser> botUser = userRepository.findById(userId);
        return botUser.map(user -> user.getRegistrationState().equals(SUCCESS)).orElse(false);
    }

    private void saveUserToDatabase(Long userId, Long chatId){
        if (userRepository.findById(userId).isPresent()){
            return;
        }
        log.info("Saving to database new user with chatId: {} and userId: {}", chatId, userId);
        BotUser botUser = BotUser.builder()
                .userId(userId)
                .chatId(chatId)
                .registrationState(RegistrationState.START_REGISTER)
                .build();
        userRepository.save(botUser);
    }

    private void changeState(Long userId, RegistrationState state) {
        log.info("Saving user to database with userId: {}", userId);
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with id: " + userId));
        botUser.setRegistrationState(state);
        userRepository.save(botUser);
    }

}
