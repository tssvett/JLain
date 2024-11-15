package dev.tssvett.schedule_bot.parsing.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Selector {
    SCHOOL_WEEK_SELECTOR(".schedule__item"),
    TIME_SELECTOR(".schedule__time"),
    LESSON_SELECTOR(".schedule__lesson"),
    GROUPS_SELECTOR(".schedule__groups"),
    TEACHER_SELECTOR(".schedule__teacher"),
    PLACE_SELECTOR(".schedule__place"),
    LESSON_TYPE_SELECTOR(".schedule__lesson-type"),
    DISCIPLINE_SELECTOR(".schedule__discipline"),
    GROUP_PAGE_SELECTOR("body > div.container.timetable > div > div > div > a"),
    FACULTY_PAGE_SELECTOR("body > div.container.timetable > div.timetable__content > div > div > div > a");

    private final String name;
}
