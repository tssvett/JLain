package dev.tssvett.schedule_bot.backend.exception.database;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class NotificationNotExistException extends ScheduleBotException {

    public NotificationNotExistException(String message) {
        super(message);
    }
}
