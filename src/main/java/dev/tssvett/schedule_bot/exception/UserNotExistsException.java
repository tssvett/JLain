package dev.tssvett.schedule_bot.exception;

public class UserNotExistsException extends RuntimeException{

    public UserNotExistsException(String message) {
        super(message);
    }
}
