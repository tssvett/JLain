package dev.tssvett.schedule_bot.backend.exception;

public class UserNotExistsException extends RuntimeException {

    public UserNotExistsException(String message) {
        super(message);
    }
}
