package dev.tssvett.schedule_bot.scheduling;

import dev.tssvett.schedule_bot.bot.TelegramBot;
import dev.tssvett.schedule_bot.entity.Notification;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.NotificationRepository;
import dev.tssvett.schedule_bot.schedule.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.schedule.lesson.Lesson;
import dev.tssvett.schedule_bot.schedule.parser.SchoolWeekParser;
import dev.tssvett.schedule_bot.schedule.utils.CurrentDateCalculator;
import dev.tssvett.schedule_bot.service.UserService;
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
@ConditionalOnProperty(name = "scheduling.notification.enabled", havingValue = "true")
public class SchedulingNotification {

    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;
    private final SchoolWeekParser schoolWeekParser;
    private final UserService userService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final CurrentDateCalculator currentDateCalculator;

    @Scheduled(fixedDelayString = "${scheduling.notification.delay}")
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
        String groupName = userService.findUserById(userId).getGroupName();
        Long groupId = userService.getUserGroupIdByGroupName(groupName);
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(groupId, currentDateCalculator.calculateWeekNumber());
        String formattedLessons = "Уведомление! Расписание на сегодня\n\n" + scheduleStringFormatter.formatDay(lessonsInWeek, currentDateCalculator.calculateTomorrowDayName());
        return SendMessage.builder()
                .chatId(userId)
                .text(formattedLessons)
                .build();
    }

    private Boolean isNotificationEnabledAndUserRegistered(Notification notification) {
        return notification.getEnabled() && notification.getBotUser().getRegistrationState()
                .equals(RegistrationState.SUCCESSFUL_REGISTRATION);
    }
}
