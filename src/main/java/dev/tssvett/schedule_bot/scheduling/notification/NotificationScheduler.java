package dev.tssvett.schedule_bot.scheduling.notification;

import dev.tssvett.schedule_bot.backend.service.LessonService;
import dev.tssvett.schedule_bot.backend.service.NotificationService;
import dev.tssvett.schedule_bot.bot.TelegramBot;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.notification.enabled", havingValue = "true")
public class NotificationScheduler {
    private final TelegramBot telegramBot;
    private final NotificationService notificationService;
    private final LessonService lessonService;
    private final ScheduleStringFormatter scheduleStringFormatter;

    @Scheduled(cron = "${scheduling.notification.cron}")
    public void sendScheduleNotificationsToUsers() {
        log.info("Staring sending notifications to users");
        telegramBot.executeBotMethod(createBotSendMessageMethods());
        log.info("Sending notifications to users finished");
    }

    private List<BotApiMethod<?>> createBotSendMessageMethods() {
        List<BotApiMethod<?>> messages = new ArrayList<>();
        notificationService.findAllEnabledNotificationsWithRegisteredStudents()
                .forEach(notification -> {
                    Long userId = notification.getStudentId();
                    log.debug("Start to send notification to user: {}", userId);
                    var weekSchedule = lessonService.getWeekScheduleMapByDate(userId);
                    messages.add(SendMessage.builder()
                            .chatId(userId)
                            .text(scheduleStringFormatter.formatNotificationMessage(weekSchedule))
                            .build());
                });
        log.info("Total notifications to send: {}", messages.size());

        return messages;
    }
}
