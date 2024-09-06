package dev.tssvett.schedule_bot.scheduling;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.entity.Lesson;
import dev.tssvett.schedule_bot.backend.entity.Notification;
import dev.tssvett.schedule_bot.backend.service.NotificationService;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.TelegramBot;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.bot.utils.CurrentDateCalculator;
import dev.tssvett.schedule_bot.parsing.SchoolWeekParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.notification.enabled", havingValue = "true")
public class SchedulingNotification {
    private final TelegramBot telegramBot;
    private final NotificationService notificationService;
    private final SchoolWeekParser schoolWeekParser;
    private final UserService userService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final CurrentDateCalculator currentDateCalculator;

    @Scheduled(fixedDelayString = "${scheduling.notification.delay}")
    public void sendScheduleNotificationsToUsers() {
        log.info("Staring sending notifications to users");
        List<Notification> notifications = notificationService.findAllNotifications();
        notifications.forEach(notification -> {
            if (notificationService.isNotificationEnabledAndUserRegistered(notification)) {
                log.info("Sending notification to user: {}", notification.getBotUser().getUserId());
                telegramBot.sendMessage(createMessageToSend(notification.getBotUser().getUserId()));
            }
        });
    }

    @Transactional
    private SendMessage createMessageToSend(Long userId) {
        BotUser botUser = userService.findUserById(userId);
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(botUser.getGroup().getGroupId(), currentDateCalculator.calculateWeekNumber());
        String formattedLessons = "Уведомление! Расписание на сегодня\n\n" + scheduleStringFormatter.formatDay(lessonsInWeek, currentDateCalculator.calculateTomorrowDayName());
        return SendMessage.builder()
                .chatId(userId)
                .text(formattedLessons)
                .build();
    }
}
