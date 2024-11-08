package dev.tssvett.schedule_bot.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.HELP_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.INFO_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.NOTIFICATION_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.PICTURE_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.REGISTER_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.START_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TODAY_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.TOMORROW_COMMAND;
import static dev.tssvett.schedule_bot.bot.enums.constants.CommandNames.WEEK_COMMAND;


@Getter
@RequiredArgsConstructor
public enum Command {
    TODAY(TODAY_COMMAND.getCommandName(), "Расписание на сегодня"),
    TOMORROW(TOMORROW_COMMAND.getCommandName(), "Расписание на завтра"),
    WEEK(WEEK_COMMAND.getCommandName(), "Расписание на неделю"),
    REGISTER(REGISTER_COMMAND.getCommandName(), "Регистрация нового пользователя"),
    START(START_COMMAND.getCommandName(), "Начало работы"),
    HELP(HELP_COMMAND.getCommandName(), "Помощь"),
    PICTURE(PICTURE_COMMAND.getCommandName(), "Рандомная картинка"),
    INFO(INFO_COMMAND.getCommandName(), "Информация о пользователе"),
    NOTIFICATION(NOTIFICATION_COMMAND.getCommandName(), "Настройка уведомлений");

    private final String commandName;
    private final String description;
}
