package dev.tssvett.schedule_bot.bot.utils.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageTextConstantsUtils {

    // Command Responses
    public static final String START_COMMAND = """
            👋 Привет бедолага самарского университета 👋
            Я бот, который будет выводить расписание и рассылать уведомления
            
            Чтобы пользоваться ботом необходимо зарегестрироваться /register
            
            Напиши мне команду /help и я расскажу тебе о своих возможностях
            """;

    public static final String HELP_COMMAND = """
            /start — позволит начать работу со мной
            /help — отобразит это сообщение с доступными командами
            /today — просмотр расписания на сегодня
            /tomorrow — просмотр расписания на завтра
            /week — просмотр расписания на неделю
            /register — регистрация нового пользователя
            /difference_notification — включить/выключить уведомления о изменениях в расписании
            /tomorrow_notification — включить/выключить уведомления о расписании на завтра
            """;

    // Registration Messages
    public static final String REGISTER_FACULTY_CHOOSING_MESSAGE = "Выбери свой факультет";
    public static final String REGISTER_CHOOSE_COURSE_MESSAGE = "Готово! Выбери свой курс";
    public static final String REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE = "Готово! Выбери свою группу";

    public static final String SUCCESSFULLY_REGISTERED_MESSAGE = """
            ❤️ Поздравляем! Вы успешно зарегистрированы! ❤️
            Расписание занятий на сегодня: /today
            """;

    public static final String ALREADY_REGISTERED_MESSAGE = """
            ❤️ Вы уже прошли регистрацию ❤️
            Желаете пройти регистрацию снова?
            """;

    // Registration State Error Messages
    public static final String FACULTY_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с факультетом
            Bаше текущее состояние регистрации не позволяет это сделать
            """;

    public static final String COURSE_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с курсом
            Bаше текущее состояние регистрации не позволяет это сделать
            """;

    public static final String GROUP_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с группой
            Bаше текущее состояние регистрации не позволяет это сделать
            """;

    // Error Messages
    public static final String UNAVAILABLE_COMMAND = """
            Извини, но я не понимаю твоей команды
            Если ты не уверен, как использовать команды, напиши /help, и я дам тебе подсказки
            """;

    public static final String NO_FACULTIES_FOUND_MESSAGE = """
            🌟 Похоже, что бот еще не нашел расписания 🌟
            Попробуйте попозже :(
            """;

    // Miscellaneous Messages
    public static final String NO_RE_REGISTRATION_ANSWER = "🌟 Похоже, в другой раз 🌟";
    public static final String SETUP_NOTIFICATION = "🔔 Настройте свои уведомления 🔔";
    public static final String NOT_ADMIN_MESSAGE = "Вы не администратор. Не трогай.....";

    //Emojis for schedule messages
    public static final String LAB_EMOJI = "🟣";
    public static final String LECTURE_EMOJI = "🟢";
    public static final String OTHER_EMOJI = "🟠";
    public static final String PRACTICE_EMOJI = "🔴";
    public static final String EXAM_EMOJI = "⚔️";
    public static final String DAY_HEADER = "🔹";
}