package dev.tssvett.schedule_bot.actions.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.repository.FacultyRepository;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardButton implements KeyboardButton {
    private final FacultyRepository facultyRepository;
    private final RegistrationService registrationService;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Faculty faculty = findFacultyById(Long.parseLong(callbackDetails.getCallbackInformation()));
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return registrationService.chooseFacultyStepCallback(userId, chatId, faculty);
    }

    private Faculty findFacultyById(Long id) {
        List<Faculty> facultyList = facultyRepository.findAll();
        for (Faculty faculty : facultyList) {
            if (id.equals(faculty.getFacultyId())) {
                return faculty;
            }
        }
        return null;
    }
}
