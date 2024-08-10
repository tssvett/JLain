package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.impl.FacultyKeyboard;
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

import static dev.tssvett.schedule_bot.enums.Action.FACULTY_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReRegistrateCallback implements KeyboardCallback {
    private final UserRepository userRepository;
    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        changeState(userId, RegistrationState.REREGISTRATE);

        //Установка кастомного коллбека тут
        String answer = callbackDetails.getCallbackText();
        log.info("Answer: {}", answer);
        if (answer.equals("Да")) {
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(new FacultyKeyboard(new FacultyParser()).createInlineKeyboard(FACULTY_CHOOSE));
            sendMessage.setText(MessageConstants.REGISTER_CHOOSE_FACULTY_MESSAGE);
            changeState(userId, RegistrationState.START);
        }
        else if (answer.equals("Нет")) {
            sendMessage.setText("Ну нет так нет...");
        }
        return sendMessage;
    }

    private void changeState(Long userId, RegistrationState state) {
        log.info("Saving user to database with userId: {}", userId);
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with id: " + userId));
        botUser.setRegistrationState(state);
        userRepository.save(botUser);
    }
}
