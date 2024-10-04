package dev.tssvett.schedule_bot.backend.exception.database;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class FacultyNotExistException extends ScheduleBotException {
    public FacultyNotExistException(String message) {
        super(message);
    }
}
