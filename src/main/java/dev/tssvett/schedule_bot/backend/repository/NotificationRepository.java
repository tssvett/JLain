package dev.tssvett.schedule_bot.backend.repository;

import dev.tssvett.schedule_bot.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
