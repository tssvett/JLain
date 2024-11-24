package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import java.util.List;

public record ScheduleDifference(
        List<LessonRecord> dbLessons,
        List<LessonRecord> parsedLessons,
        List<LessonRecord> addedLessons,
        List<LessonRecord> removedLessons
) {

}
