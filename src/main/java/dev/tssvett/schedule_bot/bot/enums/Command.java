package dev.tssvett.schedule_bot.bot.enums;

import dev.tssvett.schedule_bot.bot.enums.constants.CommandNames;
import lombok.Getter;


@Getter
public enum Command {
    TODAY(CommandNames.TODAY_COMMAND, "Расписание на сегодня"),
    TOMORROW(CommandNames.TOMORROW_COMMAND, "Расписание на завтра"),
    WEEK(CommandNames.WEEK_COMMAND, "Расписание на неделю"),
    REGISTER(CommandNames.REGISTER_COMMAND, "Регистрация нового пользователя"),
    START(CommandNames.START_COMMAND, "Начало работы"),
    HELP(CommandNames.HELP_COMMAND, "Помощь"),
    PICTURE(CommandNames.PICTURE_COMMAND, "Рандомная картинка"),
    INFO(CommandNames.INFO_COMMAND, "Информация о пользователе"),
    NOTIFICATION(CommandNames.NOTIFICATION_COMMAND, "Настройка уведомлений");

    private final String commandName;
    private final String description;

    Command(String commandName, String description) {
        this.commandName = commandName;
        this.description = description;
    }
}
