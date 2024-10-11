package dev.tssvett.schedule_bot.bot.utils.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageTextConstantsUtils {

    // Command Responses
    public static final String START_COMMAND = """
            👋 Приветствую тебя в новом мире планирования! 👋
            Я — твой новый помощник в организации времени и достижении целей.
            Чтобы начать работать со мной, просто напиши мне команду /help и я расскажу тебе обо всех своих возможностях.
            Я уверен, что вместе мы сможем сделать твой день более организованным и продуктивным""";

    public static final String HELP_COMMAND = """
            Я — твой персональный помощник в планировании. Я помогу тебе организовать время и не упустить важные события.
            Вот что я умею:
            /start — позволит начать работу со мной.
            /help — отобразит это сообщение с доступными командами.
            /today — просмотр расписания на сегодня.
            /tomorrow — просмотр расписания на завтра.
            /week — просмотр расписания на неделю.
            /picture — рандомная картинка.
            /register — регистрация нового пользователя.
            В будущем я научусь еще большему, но пока ты можешь пользоваться этим функционалом""";

    public static final String PICTURE_COMMAND = "Картинка рандомная будет верь мне";

    // Registration Messages
    public static final String REGISTER_FACULTY_CHOOSING_MESSAGE = "Для начала выбери свой факультет!";
    public static final String REGISTER_CHOOSE_COURSE_MESSAGE = "Готово! Теперь выбери свой курс!";
    public static final String REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE = "Готово! Теперь выбери свою группу!";

    public static final String SUCCESSFULLY_REGISTERED_MESSAGE = """
            ❤️ Поздравляем! Вы успешно зарегистрированы! ❤️
            Чтобы узнать своё расписание занятий используйте /today.""";

    public static final String ALREADY_REGISTERED_MESSAGE = """
            ❤️ Вы уже прошли регистрацию ❤️
            Желаете обновить свои данные и пройти регистрацию снова?""";

    // Registration State Error Messages
    public static final String FACULTY_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с факультетом.
            Bаше текущее состояние регистрации не позволяет это сделать.
            """;

    public static final String COURSE_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с курсом.
            Bаше текущее состояние регистрации не позволяет это сделать.
            """;

    public static final String GROUP_CLICK_WITH_ERROR_STATE = """
            🌟 Похоже, вы пытаетесь нажать на кнопку с группой.
            Bаше текущее состояние регистрации не позволяет это сделать.
            """;

    // Error Messages
    public static final String UNAVAILABLE_COMMAND = """
            Извини, но я не понимаю твоей команды.
            Попробуй воспользоваться одной из доступных команд.
            Если ты хочешь добавить, изменить или удалить дело в своем расписании, используй команду /schedule.
            Если ты не уверен, как использовать команды, напиши /help, и я дам тебе подсказки""";

    // Miscellaneous Messages
    public static final String NO_RE_REGISTRATION_ANSWER = "🌟 Похоже, в другой раз 🌟";
    public static final String SETUP_NOTIFICATION = "🔔 Настройте свои уведомления 🔔";
    public static final String NOT_ADMIN_MESSAGE = "Вы не администратор. Не трогай.....";
}