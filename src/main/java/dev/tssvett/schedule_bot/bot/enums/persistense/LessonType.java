package dev.tssvett.schedule_bot.bot.enums.persistense;

import java.util.HashMap;
import java.util.Map;
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
    CONSULTATION("консультация"),
    COURSEWORK("курсовые"),
    TEST("зачет"),
    INDUSTRIAL("производственная"),
    ANOTHER("другое"),
    LEARN("учебная");

    private final String name;

    private static final Map<String, LessonType> NAME_TO_TYPE_MAP = new HashMap<>();

    static {
        for (LessonType type : values()) {
            NAME_TO_TYPE_MAP.put(type.getName(), type);
        }
    }

    public static LessonType fromName(String name) {
        LessonType type = NAME_TO_TYPE_MAP.get(name);
        if (type == null) {
            throw new IllegalArgumentException(name + " is not a valid LessonType");
        }
        return type;
    }
}