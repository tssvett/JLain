package dev.tssvett.schedule_bot.actions.keyboard.impl.course;

import dev.tssvett.schedule_bot.actions.keyboard.KeyboardButtonCallback;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseKeyboardButtonCallback implements KeyboardButtonCallback {
    private final RegistrationService registrationService;

    @Override
    public SendMessage callback(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Integer courseNumber = Integer.parseInt(callbackDetails.getCallbackText());
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return registrationService.chooseCourseStepCallback(userId, chatId, courseNumber);
    }
}
