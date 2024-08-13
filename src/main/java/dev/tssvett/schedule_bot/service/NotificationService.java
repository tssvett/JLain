package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.bot.TelegramBot;
import dev.tssvett.schedule_bot.entity.Notification;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true")
public class NotificationService {

    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelayString = "${scheduling.delay}")
    public void sendScheduleNotificationsToUsers() {
        List<Notification> notifications = notificationRepository.findAll();
        notifications.forEach(notification -> {
            if (isNotificationEnabledAndUserRegistered(notification)) {
                log.info("Sending notification to user: {}", notification.getBotUser().getUserId());
                telegramBot.sendMessage(createMessageToSend(notification.getBotUser().getUserId()));
            }
        });
    }

    private SendMessage createMessageToSend(Long userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userId);
        sendMessage.setText("А вот когда нибудь тут будет уведомление на расписание");
        return sendMessage;
    }

    private Boolean isNotificationEnabledAndUserRegistered(Notification notification) {
        return notification.getEnabled() && notification.getBotUser().getRegistrationState()
                .equals(RegistrationState.SUCCESSFUL_REGISTRATION);
    }
}
