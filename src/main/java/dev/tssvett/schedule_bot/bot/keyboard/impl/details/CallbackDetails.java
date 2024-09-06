package dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details;

import dev.tssvett.schedule_bot.bot.enums.Action;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class CallbackDetails {
    private Action action;
    private String callbackInformation;

    @Override
    public String toString() {
        return action + " " + callbackInformation;
    }

    public static CallbackDetails fromString(String str) {
        List<String> parts = Arrays.stream(str.split(" ", 2)).toList();
        Action action = Action.valueOf(parts.get(0));
        String callbackText = parts.get(1);

        return CallbackDetails.builder()
                .action(action)
                .callbackInformation(callbackText)
                .build();
    }
}
