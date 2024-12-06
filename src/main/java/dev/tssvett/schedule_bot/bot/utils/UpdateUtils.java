package dev.tssvett.schedule_bot.bot.utils;

import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import java.util.Arrays;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public class UpdateUtils {

    public static long getChatIdFromCallbackQuery(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    public static long getChatIdFromMessage(Update update) {
        return update.getMessage().getChatId();
    }

    public static long getUserIdFromCallbackQuery(Update update) {
        return update.getCallbackQuery().getFrom().getId();
    }

    public static long getUserIdFromMessage(Update update) {
        return update.getMessage().getFrom().getId();
    }

    public static String getFirstWordFromMessage(Update update) {
        return update.getMessage().getText().split(" ")[0];
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

    public static boolean getTomorrowScheduleNotificationStatus(Update update) {
        String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();

        return answer.equals("Включить");
    }

    public static boolean getRefreshRegistrationStatus(Update update) {
        String answer = CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();

        return answer.equals("Да");
    }

    public static boolean messageIsText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    public static String getAdminCommand(Update update) {
        return CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation();
    }

    public static Optional<String> getArgumentsFromMessage(Update update) {
        String messageText = update.getMessage().getText();
        String[] parts = messageText.split(" ");

        if (parts.length <= 1) {
            return Optional.empty();
        }
        String arguments = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

        return Optional.of(arguments);
    }
}
