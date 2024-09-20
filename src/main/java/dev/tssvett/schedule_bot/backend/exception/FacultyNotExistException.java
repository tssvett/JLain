package dev.tssvett.schedule_bot.backend.exception;

public class FacultyNotExistException extends RuntimeException {
    public FacultyNotExistException(String message) {
        super(message);
    }
}
