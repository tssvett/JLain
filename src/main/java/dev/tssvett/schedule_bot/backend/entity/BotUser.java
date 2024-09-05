package dev.tssvett.schedule_bot.backend.entity;

import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import jakarta.persistence.Column;
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
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "course")
    private Long course;

    @Enumerated(EnumType.STRING)
    private RegistrationState registrationState;

    @OneToOne(mappedBy = "botUser")
    private Faculty faculty;

    @OneToOne(mappedBy = "botUser")
    private Group group;

    @OneToOne(mappedBy = "botUser")
    private Notification notification;
}
