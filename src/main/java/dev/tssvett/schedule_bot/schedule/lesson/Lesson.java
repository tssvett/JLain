package dev.tssvett.schedule_bot.schedule;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Lesson {
    private String name;
    private String type;
    private String place;
    private String teacher;
    private String subgroup;
    private String time;
    private String dateDay;
    private String dateNumber;
}
