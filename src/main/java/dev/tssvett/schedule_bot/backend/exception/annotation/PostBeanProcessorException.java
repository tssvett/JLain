package dev.tssvett.schedule_bot.backend.exception.annotation;

import dev.tssvett.schedule_bot.backend.exception.ScheduleBotException;

public class PostBeanProcessorException extends ScheduleBotException {

    public PostBeanProcessorException(String message) {
        super(message);
    }
}
