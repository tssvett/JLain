package dev.tssvett.schedule_bot.bot.keyboard.impl.notification;

import dev.tssvett.schedule_bot.backend.service.NotificationService;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
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
    private final NotificationService notificationService;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        boolean notificationStatus = castToBoolean(callbackDetails.getCallbackInformation());

        return createNotificationSendMessage(userId, chatId, notificationStatus);
    }

    public SendMessage createNotificationSendMessage(Long userId, Long chatId, boolean notificationStatus) {
        Notification notification = studentService.findStudentById(userId).getNotification();
        notification.setEnabled(notificationStatus);
        Notification saved = notificationService.saveNotification(notification);

        Student user = studentService.updateStudentNotification(userId, saved);
        log.info("Student {} successfully choose notification {}", user.getUserId(), notification.getEnabled());

        return SendMessage.builder()
                .chatId(chatId)
                .text("Уведомления " + (notification.getEnabled() ? "включены" : "выключены"))
                .build();
    }

    private boolean castToBoolean(String notificationStatus) {
        return notificationStatus.equals("Включить");
    }
}
