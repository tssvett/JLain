package dev.tssvett.schedule_bot.schedule.parser;

import java.util.List;

public interface Parser<T> {
    public List<T> parse();
}
