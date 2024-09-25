package dev.tssvett.schedule_bot.bot.formatter.message;

import dev.tssvett.schedule_bot.backend.dto.BotUserInfoDto;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;

public class MessageConstants {
    public static final String START_COMMAND = "\uD83D\uDC4B Приветствую тебя в новом мире планирования! \uD83D\uDC4B\n" +
            "\n" +
            "Я — твой новый помощник в организации времени и достижении целей. Я готов стать твоим личным ассистентом, который поможет тебе составить распорядок дня, следить за важными событиями и не пропустить ничего важного.\n" +
            "\n" +
            "Я помню все, что ты мне скажешь, и всегда готов помочь с планированием, даже с подбором мотивационных цитат! \uDE0A\n" +
            "\n" +
            "Чтобы начать работать со мной, просто напиши мне команду /help и я расскажу тебе обо всех моих возможностях.\n" +
            "\n" +
            "Я уверен, что вместе мы сможем сделать твой день более организованным и продуктивным. \uD83D\uDE80";

    public static final String HELP_COMMAND = "Привет! \uD83D\uDC4B Я рад, что ты решил узнать обо мне больше! \uD83D\uDE09\n" +
            "\n" +
            "Я — твой персональный помощник в планировании. Я помогу тебе организовать время и не упустить важные события.\n" +
            "\n" +
            "Вот что я умею:\n" +
            "\n" +
            "/start — позволит начать работу со мной.\n" +
            "/help — отобразит это сообщение с доступными командами.\n" +
            "/schedule — поможет тебе просмотреть расписание.\n" +
            "/picture — рандомная картинка.\n" +
            "/register — регистрация нового пользователя.\n" +
            "\n" +
            "В будущем я научусь еще большему, но пока ты можешь пользоваться этим функционалом. \uD83D\uDE09\n" +
            "\n" +
            "Надеюсь, мы с тобой отлично сработаемся! \uD83D\uDE80";

    public static final String SCHEDULE_COMMAND = "Распиcание будет верь мне";

    public static final String PICTURE_COMMAND = "Картинка рандомная будет верь мне";

    public static final String REGISTER_FACULTY_CHOOSING_MESSAGE = "Для начала выбери свой факультет!";

    public static final String UNAVAILABLE_COMMAND = "Извини, но я не понимаю твоей команды. \uD83D\uDE14\n" +
            "\n" +
            "Попробуй воспользоваться одной из доступных команд.\n" +
            "\n" +
            "Если ты хочешь добавить, изменить или удалить дело в своем расписании, используй команду /schedule.\n" +
            "\n" +
            "Если ты не уверен, как использовать команды, напиши /help, и я дам тебе подсказки. \uD83D\uDE09";

    public static final String REGISTER_CHOOSE_FACULTY_SUCCESSFULLY_MESSAGE = "Начем регистрацию. Выбери свой факультет!";
    public static final String REGISTER_CHOOSE_COURSE_MESSAGE = "Готово! Теперь выбери свой курс!";
    public static final String REGISTER_CHOOSE_GROUP_SUCCESSFULLY_MESSAGE = "Готово! Теперь выбери свою группу!";
    public static final String SUCCESSFULLY_REGISTERED_MESSAGE = """
            ❤️ Поздравляем! Вы успешно зарегистрированы! ❤️

            Чтобы узнать своё расписание занятий, просто введите команду /schedule в меню.
                        
            Если у вас возникнут вопросы, не стесняйтесь обращаться! Добро пожаловать! \uD83C\uDF1F""";

    public static final String ALREADY_REGISTERED_MESSAGE = """
            ❤️ Вы уже прошли регистрацию ❤️
            Желаете обновить свои данные и пройти регистрацию снова?""";

    public static final String REGISTRATION_CLICK_WITH_ERROR_STATE = "\uD83C\uDF1F Похоже, вы пытаетесь нажать на кнопку для повторной регистрации, но ваше текущее состояние регистрации не позволяет это сделать. Вероятно, вы не прошли регистрацию \uD83D\uDE0A";
    public static final String FACULTY_CLICK_WITH_ERROR_STATE = "\uD83C\uDF1F Похоже, вы пытаетесь нажать на кнопку с факультетом, но ваше текущее состояние регистрации не позволяет это сделать \uD83D\uDE0A";
    public static final String COURSE_CLICK_WITH_ERROR_STATE = "\uD83C\uDF1F Похоже, вы пытаетесь нажать на кнопку с курсом, но ваше текущее состояние регистрации не позволяет это сделать \uD83D\uDE0A";
    public static final String GROUP_CLICK_WITH_ERROR_STATE = "\uD83C\uDF1F Похоже, вы пытаетесь нажать на кнопку с группой, но ваше текущее состояние регистрации не позволяет это сделать \uD83D\uDE0A";
    public static final String NO_RE_REGISTRATION_ANSWER = "\uD83C\uDF1F Похоже, в другой раз \uD83C\uDF1F";

    public static final String YES = "Да";
    public static final String NO = "Нет";

    public static final String SETUP_NOTIFICATION = "\uD83D\uDD14 Настройте свои уведомления \uD83D\uDD14";
    public static final String NOT_ADMIN_MESSAGE = "Вы не администратор. Не трогай.....";

    public static String crateNotFoundUserMessage(Long userId) {
        return "🚫 Пользователь с ID " + userId + " не найден.";
    }


    public static String createInfoMessageFromParams(BotUserInfoDto botUserInfoDto) {

        return "ℹ️ **Информация о пользователе:**\n\n" +
                "👤 **ID пользователя:** " + botUserInfoDto.getUserId() + "\n\n" +
                "💬 **ID чата:** " + botUserInfoDto.getChatId() + "\n\n" +
                "🏫 **Факультет:** " + botUserInfoDto.getFaculty().getName() + "\n\n" +
                "📚 **Курс:** " + botUserInfoDto.getCourse() + "\n\n" +
                "👥 **Группа:** " + botUserInfoDto.getGroup().getName() + "\n\n" +
                "📝 **Статус регистрации:** " +
                (botUserInfoDto.getRegistrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION) ? "✅ Успешно пройдена" : "❌ Не завершена") + "\n\n" +
                "🔔 **Уведомления:** " + (botUserInfoDto.getNotification().getEnabled() ? "✅ Включены" : "❌ Выключены");
    }


}
