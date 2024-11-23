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

    public List<BotApiMethod<?>> createTomorrowScheduleNotificationsMessages() {
        List<BotApiMethod<?>> messages = new ArrayList<>();
        this.findAllTomorrowScheduleWithRegisteredStudents()
                .forEach(notification -> {
                    Long userId = notification.getStudentId();
                    log.debug("Start to send notification to user: {}", userId);
                    var weekSchedule = lessonService.getWeekScheduleMapByDate(userId);
                    messages.add(SendMessage.builder()
                            .chatId(userId)
                            .text(scheduleStringFormatter.formatToTomorrowNotificationMessage(weekSchedule))
                            .build());
                });
        log.info("Total notifications to send: {}", messages.size());

        return messages;
    }

    public List<BotApiMethod<?>> createScheduleDifferenceNotificationsMessages() {
        List<BotApiMethod<?>> messages = new ArrayList<>();
        this.findAllEnabledScheduleDifferenceWithRegisteredStudents()
                .forEach(notification -> {
                    Long userId = notification.getStudentId();
                    log.debug("Start to send notification to user: {}", userId);
                    var difference = lessonService.findScheduleDifference(userId);
                    messages.add(SendMessage.builder()
                            .chatId(userId)
                            .text(scheduleStringFormatter.formatToScheduleDifference(difference))
                            .build());
                });
        log.info("Total notifications to send: {}", messages.size());

        return messages;
    }
}
