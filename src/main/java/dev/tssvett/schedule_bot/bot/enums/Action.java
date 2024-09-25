package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;

@Getter
public enum Action {
    FACULTY_CHOOSE("faculty"),
    COURSE_CHOOSE("course"),
    GROUP_CHOOSE("group"),
    REREGISTRATE("reregistrate"),
    NOTIFICATION("notification");

    private final String actionName;

    Action(String actionName) {
        this.actionName = actionName;
    }
}