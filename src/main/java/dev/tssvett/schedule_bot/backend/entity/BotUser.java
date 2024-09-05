package dev.tssvett.schedule_bot.entity;

import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bot_user")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BotUser {
    @Id
    Long userId;
    Long chatId;
    String facultyName;
    String groupName;
    String course;

    @Enumerated(EnumType.STRING)
    RegistrationState registrationState;

    @OneToOne(mappedBy = "botUser")
    private Notification notification;
}
