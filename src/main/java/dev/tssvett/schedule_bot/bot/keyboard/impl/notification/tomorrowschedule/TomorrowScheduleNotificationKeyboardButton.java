package dev.tssvett.schedule_bot.bot.keyboard.impl.notification.tomorrowschedule;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TomorrowScheduleNotificationKeyboardButton implements KeyboardButton {
    private final StudentService studentService;

    @Override
    public SendMessage onButtonClick(Update update) {
        long userId = UpdateUtils.getUserIdFromCallbackQuery(update);
        long chatId = UpdateUtils.getChatIdFromCallbackQuery(update);
        boolean notificationStatus = UpdateUtils.getTomorrowScheduleNotificationStatus(update);

        return processNotificationSelectionOnButtonClick(userId, chatId, notificationStatus);
    }

    private SendMessage processNotificationSelectionOnButtonClick(long userId, long chatId, boolean notificationStatus) {
        studentService.updateTomorrowScheduleNotificationStatus(userId, notificationStatus);

        return sendCurrentNotificationStatusMessage(chatId, notificationStatus);
    }

    private SendMessage sendCurrentNotificationStatusMessage(long chatId, boolean notificationStatus) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Уведомления для ежедневного расписания " + (notificationStatus ? "включены" : "отключены"))
                .build();
    }
}
