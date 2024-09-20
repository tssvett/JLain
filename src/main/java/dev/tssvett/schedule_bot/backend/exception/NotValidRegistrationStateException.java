package dev.tssvett.schedule_bot.backend.exception;

public class NotValidRegistrationStateException extends RuntimeException {

    public NotValidRegistrationStateException(String message) {
        super(message);
    }
}
