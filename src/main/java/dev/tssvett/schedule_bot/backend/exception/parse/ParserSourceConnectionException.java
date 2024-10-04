package dev.tssvett.schedule_bot.backend.exception.parse;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class ParserSourceConnectionException extends ScheduleBotException {

    public ParserSourceConnectionException(String message) {
        super(message);
    }
}
