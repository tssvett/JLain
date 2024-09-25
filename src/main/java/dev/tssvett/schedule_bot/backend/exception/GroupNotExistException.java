package dev.tssvett.schedule_bot.backend.exception;

public class GroupNotExistException extends RuntimeException {
    public GroupNotExistException(String message) {
        super(message);
    }
}
