package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.impl.GroupKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.faculty.Faculty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.enums.Action.GROUP_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardCallback implements KeyboardCallback {

    private final GroupKeyboard groupKeyboard;
    private final UserRepository userRepository;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //Установка кастомного коллбека тут
        Integer courseNumber = Integer.parseInt(callbackDetails.getCallbackText());
        try {
            saveUserToDatabase(userId, courseNumber);
        }
        catch (NotValidRegistrationStateException e){
            log.warn(e.getMessage());
            sendMessage.setText("Вы уже выбрали курс, не надо сюда жмать. Выбирайте группу");
            return sendMessage;
        }
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE);
        sendMessage.setReplyMarkup(groupKeyboard.createInlineKeyboard(GROUP_CHOOSE));
        return sendMessage;
    }

    private void saveUserToDatabase(Long userId, Integer courseNumber) {
        log.info("Saving user to database with userId: {} and courseNumber: {}", userId, courseNumber);
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.FACULTY_CHOOSED)){
            throw new NotValidRegistrationStateException("User clicked on already chosen course");
        }
        botUser.setCourse(courseNumber.toString());
        botUser.setRegistrationState(RegistrationState.COURSE_CHOOSED);
        userRepository.save(botUser);
    }

}