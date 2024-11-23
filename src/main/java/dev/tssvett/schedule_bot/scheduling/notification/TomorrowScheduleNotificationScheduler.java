package dev.tssvett.schedule_bot.scheduling.notification;

import dev.tssvett.schedule_bot.backend.service.NotificationService;
import dev.tssvett.schedule_bot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.notification.enabled", havingValue = "true")
public class TomorrowScheduleNotificationScheduler {
    private final NotificationService notificationService;
    private final TelegramBot telegramBot;

    @Scheduled(cron = "${scheduling.notification.cron}")
    public void sendScheduleNotificationsToUsers() {
        log.info("Staring sending notifications to users");
        telegramBot.sendMessage(notificationService.createNotificationsMessages());
        log.info("Sending notifications to users finished");
    }
}
