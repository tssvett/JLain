package dev.tssvett.schedule_bot.backend.exception;

public class NotificationNotExistsException extends RuntimeException {
    public NotificationNotExistsException(String s) {
        super(s);
    }
}
