package dev.tssvett.schedule_bot.enums;

import lombok.Getter;

@Getter
public enum Action {
    FACULTY_CHOOSE("faculty"),
    COURSE_CHOOSE("course"),
    GROUP_CHOOSE("group");


    private final String actionName;

    Action(String actionName) {
        this.actionName = actionName;
    }
}
