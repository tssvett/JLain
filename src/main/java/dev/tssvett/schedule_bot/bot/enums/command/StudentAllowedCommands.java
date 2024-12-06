package dev.tssvett.schedule_bot.bot.enums.command;

import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.DIFFERENCE_SCHEDULE_NOTIFICATION_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.HELP_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.INFO_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.REGISTER_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.START_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TODAY_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TOMORROW_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TOMORROW_SCHEDULE_NOTIFICATION_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.WEEK_COMMAND;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum StudentAllowedCommands {
    TODAY(TODAY_COMMAND.getCommandName(), "Расписание на сегодня"),
    TOMORROW(TOMORROW_COMMAND.getCommandName(), "Расписание на завтра"),
    WEEK(WEEK_COMMAND.getCommandName(), "Расписание на неделю"),
    REGISTER(REGISTER_COMMAND.getCommandName(), "Регистрация нового пользователя"),
    START(START_COMMAND.getCommandName(), "Начало работы"),
    HELP(HELP_COMMAND.getCommandName(), "Помощь"),
    INFO(INFO_COMMAND.getCommandName(), "Информация о пользователе"),
    TOMORROW_SCHEDULE_NOTIFICATION_SETTINGS(TOMORROW_SCHEDULE_NOTIFICATION_COMMAND.getCommandName(),
            "Настройка ежедневной рассылки расписания"),
    DIFFERENCE_SCHEDULE_NOTIFICATION_SETTINGS(DIFFERENCE_SCHEDULE_NOTIFICATION_COMMAND.getCommandName(),
            "Настройка проверки на изменения в расписании");

    private final String commandName;
    private final String description;
}
