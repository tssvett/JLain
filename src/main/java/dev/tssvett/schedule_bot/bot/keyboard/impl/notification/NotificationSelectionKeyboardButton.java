package dev.tssvett.schedule_bot.bot.keyboard.impl.notification;

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
public class NotificationSelectionKeyboardButton implements KeyboardButton {
    private final StudentService studentService;

    @Override
    public SendMessage onButtonClick(Update update) {
        long userId = UpdateUtils.getUserId(update);
        long chatId = UpdateUtils.getChatId(update);
        boolean notificationStatus = UpdateUtils.getNotificationStatus(update);

        return processNotificationSelectionOnButtonClick(userId, chatId, notificationStatus);
    }

    private SendMessage processNotificationSelectionOnButtonClick(long userId, long chatId, boolean notificationStatus) {
        studentService.updateStudentNotification(userId, notificationStatus);

        return sendCurrentNotificationStatusMessage(chatId, notificationStatus);
    }

    private SendMessage sendCurrentNotificationStatusMessage(long chatId, boolean notificationStatus) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Уведомления " + (notificationStatus ? "включены" : "отключены"))
                .build();
    }
}
