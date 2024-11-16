package dev.tssvett.schedule_bot.parsing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Connection {
    USER_AGENT("Mozilla/5.0");

    private final String name;
}
