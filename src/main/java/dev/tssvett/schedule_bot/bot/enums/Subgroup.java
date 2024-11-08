package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum Subgroup {
    FIRST("1"),
    SECOND("2"),
    EMPTY("");

    private final String name;

    private static final Map<String, Subgroup> NAME_TO_SUBGROUP_MAP = new HashMap<>();

    static {
        for (Subgroup subgroup : values()) {
            NAME_TO_SUBGROUP_MAP.put(subgroup.getName(), subgroup);
        }
    }

    public static Subgroup fromName(String name) {
        Subgroup subgroup = NAME_TO_SUBGROUP_MAP.get(name);
        if (subgroup == null) {
            throw new IllegalArgumentException(name + " is not a valid Subgroup");
        }
        return subgroup;
    }
}