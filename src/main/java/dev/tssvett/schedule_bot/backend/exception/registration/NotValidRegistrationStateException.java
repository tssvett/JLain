package dev.tssvett.schedule_bot.backend.exception.registration;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class NotValidRegistrationStateException extends ScheduleBotException {

    public NotValidRegistrationStateException(String message) {
        super(message);
    }
}
