package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.persistence.model.tables.Notification;
import dev.tssvett.schedule_bot.persistence.model.tables.Student;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final DSLContext dslContext;

    public List<NotificationRecord> findAll() {
        return dslContext.select()
                .from(Notification.NOTIFICATION)
                .fetchInto(NotificationRecord.class);
    }

    public Optional<NotificationRecord> findById(Long notificationId) {
        return dslContext.selectFrom(Notification.NOTIFICATION)
                .where(Notification.NOTIFICATION.ID.eq(notificationId))
                .fetchOptional();
    }

    public NotificationRecord save(NotificationRecord notification) {
        return dslContext.insertInto(Notification.NOTIFICATION)
                .set(Notification.NOTIFICATION.ENABLED, notification.getEnabled())
                .returning(Notification.NOTIFICATION.ID)
                .fetchOne();
    }


    public List<NotificationRecord> findAllEnabledNotificationsWithRegisteredStudents() {
        return dslContext.select()
                .from(Notification.NOTIFICATION)
                .join(Student.STUDENT)
                .on(Notification.NOTIFICATION.ID.eq(Student.STUDENT.NOTIFICATION_ID))
                .where(Notification.NOTIFICATION.ENABLED.isTrue()).and(Student.STUDENT.REGISTRATION_STATE.eq(RegistrationState.SUCCESSFUL_REGISTRATION.name()))
                .fetchInto(NotificationRecord.class);
    }

    public void update(Long notificationId, Boolean enabled) {
        dslContext.update(Notification.NOTIFICATION)
                .set(Notification.NOTIFICATION.ENABLED, enabled)
                .where(Notification.NOTIFICATION.ID.eq(notificationId))
                .execute();
    }
}
