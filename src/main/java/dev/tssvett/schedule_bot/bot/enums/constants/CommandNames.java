package dev.tssvett.schedule_bot.bot.enums.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandNames {
    START_COMMAND("/start"),
    HELP_COMMAND("/help"),
    TODAY_COMMAND("/today"),
    TOMORROW_COMMAND("/tomorrow"),
    WEEK_COMMAND("/week"),
    REGISTER_COMMAND("/register"),
    PICTURE_COMMAND("/picture"),
    INFO_COMMAND("/info"),
    NOTIFICATION_COMMAND("/notification");

    private final String commandName;
}
