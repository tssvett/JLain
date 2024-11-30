package dev.tssvett.schedule_bot.bot.utils.message;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;
import dev.tssvett.schedule_bot.bot.enums.persistense.Subgroup;
import static dev.tssvett.schedule_bot.bot.utils.StringUtils.capitalizeFirstLetter;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageCreateUtils {

    public static String createInfoCommandMessageText
            (StudentInfoDto studentInfoDto,
             String facultyName,
             String groupName,
             boolean tomorrowNotification,
             boolean differenceNotification,
             Role role) {
        return """
                ℹ️ **Информация о пользователе:**
                👤 **ID пользователя:** %d
                💬 **ID чата:** %d
                🏫 **Факультет:** %s
                📚 **Курс:** %s
                👥 **Группа:** %s
                📝 **Статус регистрации:** %s
                🔔 **Уведомления на завтра:** %s
                🔔 **Уведомления на изменения:** %s
                👤 **Роль:** %s""".formatted(
                studentInfoDto.userId(),
                studentInfoDto.chatId(),
                facultyName,
                studentInfoDto.course(),
                groupName,
                studentInfoDto.registrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION)
                        ? "✅ Успешно пройдена"
                        : "❌ Не завершена",
                tomorrowNotification
                        ? "✅ Включены"
                        : "❌ Отключены",
                differenceNotification
                        ? "✅ Включены"
                        : "❌ Отключены",
                role.getValue()
        );
    }

    public static String createDayHeader(String day, String lessonDate) {
        return String.format("""
                %s %s (%s)
                                
                """, MessageTextConstantsUtils.DAY_HEADER, capitalizeFirstLetter(day), lessonDate);
    }

    public static String createStringLesson(LessonInfoDto lesson) {
        return String.format("""
                        %s %s | %s | %s%s
                                    
                        """, getEmojiForLesson(lesson), lesson.time(), capitalizeFirstLetter(lesson.name()),
                lesson.place(),
                lesson.subgroup().equals(Subgroup.EMPTY) ? "" : "\nПодгруппа: " + lesson.subgroup().getName());
    }

    public static String createNotificationMessage(String formattedDay) {
        return String.format("""
                Уведомление! Расписание на завтра
                            
                %s
                """, formattedDay);
    }

    public static String createNotExistingDayMessage(String weekDayName, String currentDate) {
        return String.format("""
                %s %s (%s):
                Зачилься, пар нет :)
                                
                """, MessageTextConstantsUtils.DAY_HEADER, capitalizeFirstLetter(weekDayName), currentDate);
    }

    private static String getEmojiForLesson(LessonInfoDto lesson) {
        return switch (lesson.type()) {
            case LABORATORY -> MessageTextConstantsUtils.LAB_EMOJI;
            case LECTURE -> MessageTextConstantsUtils.LECTURE_EMOJI;
            case PRACTICE -> MessageTextConstantsUtils.PRACTICE_EMOJI;
            case ANOTHER -> MessageTextConstantsUtils.OTHER_EMOJI;
            case EXAM -> MessageTextConstantsUtils.EXAM_EMOJI;
            default -> "";
        };
    }

    public static String createRegisteredStudentsMessage(List<StudentInfoDto> studentsInfoList) {
        return String.format("""
                🍀 Количество зарегестрированных пользователей: %s
                """, studentsInfoList.size());
    }

    public static String createAdminMessage() {
        return String.format("""
                ⚙ Доступные команды для админа ⚙
                """);
    }

    public static String createSendMessageToUsersMessage(List<Long> studentIds) {
        return String.format("""
                🍀 Сообщение добавлено в базу для дальнейшей рассылки %s пользователям
                """, studentIds.size());
    }
}
