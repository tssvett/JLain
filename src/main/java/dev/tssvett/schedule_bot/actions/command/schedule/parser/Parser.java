package dev.tssvett.schedule_bot.actions.command.schedule.parser;

import java.util.List;

public interface Parser<T> {
    public List<T> parse();
}
