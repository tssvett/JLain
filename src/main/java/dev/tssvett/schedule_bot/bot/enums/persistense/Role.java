package dev.tssvett.schedule_bot.bot.enums.persistense;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STUDENT("Студент"),
    ADMIN("Администратор");

    private final String value;
}
