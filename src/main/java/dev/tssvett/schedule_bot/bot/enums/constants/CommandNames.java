package dev.tssvett.schedule_bot.bot.enums.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    TOMORROW_SCHEDULE_NOTIFICATION_COMMAND("/tomorrow_notification"),
    DIFFERENCE_SCHEDULE_NOTIFICATION_COMMAND("/difference_notification");

    private final String commandName;

    private static final Map<String, CommandNames> NAME_TO_COMMAND_MAP = new HashMap<>();

    static {
        for (CommandNames command : values()) {
            NAME_TO_COMMAND_MAP.put(command.getCommandName(), command);
        }
    }

    public static Optional<CommandNames> fromCommandName(String commandName) {
        CommandNames command = NAME_TO_COMMAND_MAP.get(commandName);

        return Optional.ofNullable(command);
    }
}