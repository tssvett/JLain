package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum LessonType {
    LABORATORY("лабораторная"),
    LECTURE("лекция"),
    PRACTICE("практика"),
    EXAM("экзамен"),
    EMPTY(""),
    ANOTHER("другое");

    private final String name;

    public static LessonType fromName(String name) {
        for (LessonType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid LessonType");
    }
}
