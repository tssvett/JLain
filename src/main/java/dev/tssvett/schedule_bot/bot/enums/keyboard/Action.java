package dev.tssvett.schedule_bot.bot.enums.keyboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Action {
    FACULTY_CHOOSE("faculty"),
    COURSE_CHOOSE("course"),
    GROUP_CHOOSE("group"),
    REFRESH_REGISTRATION("refresh_registration"),
    TOMORROW_SCHEDULE_NOTIFICATION("notification"),
    SCHEDULE_DIFFERENCE_NOTIFICATION("schedule_difference_notification"),
    ADMIN_COMMAND_SELECTION("admin_command_selection");

    private final String actionName;
}
