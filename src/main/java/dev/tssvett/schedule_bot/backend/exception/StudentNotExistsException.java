package dev.tssvett.schedule_bot.backend.exception;

public class StudentNotExistsException extends RuntimeException {

    public StudentNotExistsException(String message) {
        super(message);
    }
}
