package dev.tssvett.schedule_bot.scheduling;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.service.NotificationService;
import dev.tssvett.schedule_bot.backend.service.ScheduleService;
import dev.tssvett.schedule_bot.bot.TelegramBot;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
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
    private final ScheduleService scheduleService;
    private final ScheduleStringFormatter scheduleStringFormatter;
    private final DateUtils dateUtils;

    @Scheduled(fixedDelayString = "${scheduling.notification.delay}")
    public void sendScheduleNotificationsToUsers() {
        log.info("Staring sending notifications to users");
        /*TODO: 1) сделать специальный запрос в бд, который будет вытаскивать только те уведомления, которые включены
                2) Выводить количество юзеров которым идет рассылка
        */
        List<Notification> notifications = notificationService.findAllNotifications();

        notifications.forEach(notification -> {
            if (notificationService.isNotificationEnabledAndUserRegistered(notification)) {
                log.info("Sending notification to user: {}", notification.getStudent().getUserId());
                /*
                 TODO: добавить боту возможность множественной рассылки
                    Сейчас бот ловит только SendMessage, а List<SendMessage> не видит
                    Нужно изменить SendMessage на BotApiMethod<?>
                    Тогда отсюда можно будет убрать telegramBot, и возвращать просто List<BotApiMethod<?>>
                 */
                telegramBot.executeBotMethod(createMessageToSend(notification.getStudent().getUserId()));
            }
        });
    }

    @Transactional
    private SendMessage createMessageToSend(Long userId) {
        List<LessonInfoDto> lessonsInWeek = scheduleService.getWeekSchedule(userId);
        String formattedLessons = formatLessonsForTomorrow(lessonsInWeek);

        return SendMessage.builder()
                .chatId(userId)
                .text(formattedLessons)
                .build();
    }

    private String formatLessonsForTomorrow(List<LessonInfoDto> lessonsInWeek) {
        String tomorrowDayName = dateUtils.calculateTomorrowDayName();
        String formattedDay = scheduleStringFormatter.formatDay(lessonsInWeek, tomorrowDayName);

        return String.format("""
            Уведомление! Расписание на завтра
            
            %s
            """, formattedDay);
    }
}
