package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final LessonService lessonService;
    private final ScheduleStringFormatter scheduleStringFormatter;

    public List<NotificationRecord> findAllTomorrowScheduleWithRegisteredStudents() {
        return notificationRepository.findAllEnabledTomorrowScheduleWithRegisteredStudents();
    }

    public List<NotificationRecord> findAllEnabledScheduleDifferenceWithRegisteredStudents() {
        return notificationRepository.findAllEnabledScheduleDifferenceWithRegisteredStudents();
    }

    public List<SendMessage> createTomorrowScheduleNotificationsMessages() {
        List<SendMessage> messages = new ArrayList<>();
        this.findAllTomorrowScheduleWithRegisteredStudents()
                .forEach(notification -> {
                    Long userId = notification.getStudentId();
                    log.debug("Start to send notification to user: {}", userId);
                    var weekSchedule = lessonService.getWeekScheduleMapByDate(userId);
                    messages.add(buildDifferenceMessage(userId, scheduleStringFormatter.formatToTomorrowNotificationMessage(weekSchedule)));
                });
        log.info("Total notifications to send: {}", messages.size());

        return messages;
    }

    public List<SendMessage> createScheduleDifferenceNotificationsMessages() {
        List<SendMessage> messages = new ArrayList<>();
        List<NotificationRecord> notifications = this.findAllEnabledScheduleDifferenceWithRegisteredStudents();

        for (NotificationRecord notification : notifications) {
            Long userId = notification.getStudentId();
            log.debug("Start to send notification to user: {}", userId);
            lessonService.findScheduleDifference(userId).ifPresent(
                    difference -> {
                        if (!(difference.removedLessons().isEmpty() && difference.addedLessons().isEmpty())) {
                            messages.add(buildDifferenceMessage(userId,
                                    scheduleStringFormatter.formatToScheduleDifference(difference))
                            );
                        }
                    }
            );
        }
        log.info("Total notifications to send: {}", messages.size());

        return messages;
    }

    private SendMessage buildDifferenceMessage(Long userId, String scheduleStringFormatter) {
        return SendMessage.builder()
                .chatId(userId)
                .text(scheduleStringFormatter)
                .build();
    }
}
