package dev.tssvett.schedule_bot.actions.keyboard.callback.details;

import dev.tssvett.schedule_bot.enums.Action;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class CallbackDetails {
    private Action action;
    private String callbackText;

    @Override
    public String toString() {
        return action + " " + callbackText;
    }

    public static CallbackDetails fromString(String str) {
        List<String> parts = Arrays.stream(str.split(" ", 2)).toList();
        System.out.println(parts);
        Action action = Action.valueOf(parts.get(0));
        String callbackText = parts.get(1);

        return CallbackDetails.builder()
                .action(action)
                .callbackText(callbackText)
                .build();
    }
}
