package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateUtils {

    public static Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    public static Long getUserId(Update update) {
        return update.getCallbackQuery().getFrom().getId();
    }

    public static String getData(Update update) {
        return update.getCallbackQuery().getData();
    }

    public static Long getFacultyId(Update update) {
        try {
            return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getGroupId(Update update) {
        try {
            return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getCourse(Update update) {
        try {
            return Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getNotificationStatus(Update update) {
        try {
            String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();
            return answer.equals("Включить");
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean getResreshRegistrationStatus(Update update) {
        try {
            String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();
            //представим что он бегает по списку разрешенных слов, которых много
            //но сейчас оно ровно 1
            return answer.equals("Да");
        } catch (Exception e) {
            return null;
        }
    }
}
