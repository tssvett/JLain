package dev.tssvett.schedule_bot.bot.utils.message;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;
import lombok.experimental.UtilityClass;

import static dev.tssvett.schedule_bot.bot.utils.StringUtils.capitalizeFirstLetter;

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
}
