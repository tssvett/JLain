package dev.tssvett.schedule_bot.enums;

import lombok.Getter;

@Getter
public enum Commands {
    START("/start"),
    HELP("/help");

    private final String command;

    Commands(String command) {
        this.command = command;
    }
}
