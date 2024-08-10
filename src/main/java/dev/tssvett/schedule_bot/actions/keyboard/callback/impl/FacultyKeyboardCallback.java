package dev.tssvett.schedule_bot.actions.keyboard.callback.impl;

import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.actions.keyboard.callback.KeyboardCallback;
import dev.tssvett.schedule_bot.actions.keyboard.impl.CourseKeyboard;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.exception.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.repository.UserRepository;
import dev.tssvett.schedule_bot.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static dev.tssvett.schedule_bot.enums.Action.COURSE_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardCallback implements KeyboardCallback {
    private final FacultyParser facultyParser;
    private final CourseKeyboard courseKeyboard;
    private final UserRepository userRepository;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        //Установка кастомного коллбека тут
        Faculty faculty = findFacultyById(callbackDetails.getCallbackText());
        try {
            saveUserToDatabase(userId, faculty);
        }
        catch (NotValidRegistrationStateException e){
            log.warn(e.getMessage());
            sendMessage.setText("Вы уже выбрали факультет, не надо сюда жмать. Выбирайте курс");
            return sendMessage;
        }
        sendMessage.setText(MessageConstants.REGISTER_CHOOSE_COURSE_MESSAGE);
        sendMessage.setReplyMarkup(courseKeyboard.createInlineKeyboard(COURSE_CHOOSE));

        return sendMessage;
    }

    private Faculty findFacultyById(String id) {
        List<Faculty> facultyList = facultyParser.parse();
        for (Faculty faculty : facultyList) {
            if (id.equals(faculty.getId())) {
                return faculty;
            }
        }
        return null;
    }


    private void saveUserToDatabase(Long userId, Faculty faculty){
        log.info("Saving user to database with userId: {} and faculty: {}", userId, faculty);
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user with id: " + userId));
        if (!botUser.getRegistrationState().equals(RegistrationState.START_REGISTER)){
            throw new NotValidRegistrationStateException("User clicked on already chosen faculty");
        }
        botUser.setFacultyName(faculty.getName());
        botUser.setRegistrationState(RegistrationState.FACULTY_CHOOSED);
        userRepository.save(botUser);
    }
}
