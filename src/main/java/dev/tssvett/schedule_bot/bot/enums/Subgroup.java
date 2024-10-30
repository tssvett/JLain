package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Subgroup {
    FIRST("1"),
    SECOND("2"),
    EMPTY("");

    private final String name;

    public static Subgroup fromName(String name) {
        for (Subgroup type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid LessonType");
    }
}
