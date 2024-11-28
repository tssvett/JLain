package dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details;

import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import lombok.Builder;
import lombok.Data;

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
        String[] parts = str.split(" ", 2);
        Action action = Action.valueOf(parts[0]);
        String callbackText = parts[1];

        return CallbackDetails.builder()
                .action(action)
                .callbackInformation(callbackText)
                .build();
    }
}
