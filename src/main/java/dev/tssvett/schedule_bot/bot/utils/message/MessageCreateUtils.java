package dev.tssvett.schedule_bot.bot.utils.message;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageCreateUtils {

    public static String createInfoCommandMessageText(StudentInfoDto studentInfoDto) {
        return """
                ℹ️ **Информация о пользователе:**
                👤 **ID пользователя:** %d
                💬 **ID чата:** %d
                🏫 **Факультет:** %s
                📚 **Курс:** %s
                👥 **Группа:** %s
                📝 **Статус регистрации:** %s
                🔔 **Уведомления:** %s""".formatted(
                studentInfoDto.userId(),
                studentInfoDto.chatId(),
                studentInfoDto.faculty().name(),
                studentInfoDto.course(),
                studentInfoDto.group().name(),
                (studentInfoDto.registrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION) ? "✅ Успешно пройдена" : "❌ Не завершена"),
                (studentInfoDto.notification().enabled() ? "✅ Включены" : "❌ Отключены")
        );
    }
}
