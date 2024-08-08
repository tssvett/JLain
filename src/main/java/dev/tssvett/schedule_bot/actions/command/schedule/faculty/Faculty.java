package dev.tssvett.schedule_bot.actions.command.schedule.faculty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Faculty {
    private String name;
    private String id;
}
