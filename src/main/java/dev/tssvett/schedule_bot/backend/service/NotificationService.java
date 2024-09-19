package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.entity.Notification;
import dev.tssvett.schedule_bot.backend.repository.NotificationRepository;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
    }

    public Boolean isNotificationEnabledAndUserRegistered(Notification notification) {
        return notification.getEnabled() && notification.getBotUser().getRegistrationState()
                .equals(RegistrationState.SUCCESSFUL_REGISTRATION);
    }
}
