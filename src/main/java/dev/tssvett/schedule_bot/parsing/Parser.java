package dev.tssvett.schedule_bot.parsing;

import java.util.List;

public interface Parser<T> {
    public List<T> parse();
}
