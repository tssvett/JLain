package dev.tssvett.schedule_bot.schedule.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {
    private String name;
    private String id;
}
