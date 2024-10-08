package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UpdateUtils {

    public static long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    public static long getUserId(Update update) {
        return update.getCallbackQuery().getFrom().getId();
    }

    public static String getData(Update update) {
        return update.getCallbackQuery().getData();
    }

    public static long getFacultyId(Update update) {
        return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
    }

    public static long getGroupId(Update update) {
        return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
    }

    public static long getCourse(Update update) {
        return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
    }

    public static boolean getNotificationStatus(Update update) {
        String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();
        return answer.equals("Включить");
    }

    public static boolean getRefreshRegistrationStatus(Update update) {
        String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();
        //представим что он бегает по списку разрешенных слов, которых много
        //но сейчас оно ровно 1
        return answer.equals("Да");
    }
}
