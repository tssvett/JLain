package dev.tssvett.schedule_bot.backend.exception.database;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class GroupNotExistException extends ScheduleBotException {
    public GroupNotExistException(String message) {
        super(message);
    }
}
