package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Action {
    FACULTY_CHOOSE("faculty"),
    COURSE_CHOOSE("course"),
    GROUP_CHOOSE("group"),
    REFRESH_REGISTRATION("refresh_registration"),
    NOTIFICATION("notification");

    private final String actionName;
}
