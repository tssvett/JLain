package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.tables.Lesson;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LessonRepository {
    private final DSLContext dslContext;

    public void saveAll(List<LessonRecord> lessons) {
        try{
            dslContext.batchInsert(lessons).execute();
        } catch (DuplicateKeyException e) {
            log.error("Error while saving lessons: {}", e.getMessage());
        }
    }
}
