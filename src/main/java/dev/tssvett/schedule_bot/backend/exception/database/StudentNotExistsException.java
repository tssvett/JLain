package dev.tssvett.schedule_bot.backend.exception.database;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class StudentNotExistsException extends ScheduleBotException {

    public StudentNotExistsException(String message) {
        super(message);
    }
}
