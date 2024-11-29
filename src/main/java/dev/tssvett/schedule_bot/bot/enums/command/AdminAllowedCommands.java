package dev.tssvett.schedule_bot.bot.enums.command;

import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.SEND_MESSAGE_TO_USERS_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.SHOW_REGISTERED_USERS_COMMAND;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminAllowedCommands {
    SHOW_STUDENTS_COMMAND(SHOW_REGISTERED_USERS_COMMAND.getCommandName(),
            "Показать список зарегистрированных пользователей"),
    SEND_MESSAGE_TO_STUDENTS_COMMAND(SEND_MESSAGE_TO_USERS_COMMAND.getCommandName(),
            "Отправить сообщение всем студентам");

    private final String commandName;
    private final String description;
}
