package dev.tssvett.schedule_bot.bot.enums.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    INFO_COMMAND("/info"),
    TOMORROW_SCHEDULE_NOTIFICATION_COMMAND("/tomorrow_notification"),
    DIFFERENCE_SCHEDULE_NOTIFICATION_COMMAND("/difference_notification"),
    SHOW_REGISTERED_USERS_COMMAND("/show_registered_users"),
    ADMIN_COMMAND("/admin"),
    SEND_MESSAGE_TO_USERS_COMMAND("/send_message_to_users");


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