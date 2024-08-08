package dev.tssvett.schedule_bot.actions.command.schedule.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {
    private String name;
    private String id;
}
