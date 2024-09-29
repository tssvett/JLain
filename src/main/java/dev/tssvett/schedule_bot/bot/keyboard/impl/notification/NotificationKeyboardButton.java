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
public class NotificationKeyboardButton implements KeyboardButton {
    private final StudentService studentService;

    @Override
    public SendMessage click(Update update) {
        Long userId = UpdateUtils.getUserId(update);
        Long chatId = UpdateUtils.getChatId(update);
        Boolean notificationStatus = UpdateUtils.getNotificationStatus(update);

        return handleClick(userId, chatId, notificationStatus);
    }

    private SendMessage handleClick(Long userId, Long chatId, Boolean notificationStatus) {
        studentService.updateStudentNotification(userId, notificationStatus);

        return changeNotificationSendMessage(chatId, notificationStatus);
    }

    private SendMessage changeNotificationSendMessage(Long chatId, Boolean notificationStatus) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Уведомления " + (notificationStatus ? "включены" : "отключены"))
                .build();
    }
}
