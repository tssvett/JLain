package dev.tssvett.schedule_bot.exception;

public class GroupNotExistException extends RuntimeException {
    public GroupNotExistException(String message) {
        super(message);
    }
}
