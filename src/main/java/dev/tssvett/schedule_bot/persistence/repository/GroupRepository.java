package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.tables.EducationalGroup;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GroupRepository {
    private final DSLContext dslContext;

    public List<EducationalGroupRecord> findAll() {
        return dslContext.select()
                .from(EducationalGroup.EDUCATIONAL_GROUP)
                .fetchInto(EducationalGroupRecord.class);
    }

    public Optional<EducationalGroupRecord> findById(Long groupId) {
        return dslContext.selectFrom(EducationalGroup.EDUCATIONAL_GROUP)
                .where(EducationalGroup.EDUCATIONAL_GROUP.GROUP_ID.eq(groupId))
                .fetchOptional();
    }

    public void saveAll(List<EducationalGroupRecord> educationalGroups) {
        for (EducationalGroupRecord educationalGroup : educationalGroups) {
            try {
                dslContext.insertInto(EducationalGroup.EDUCATIONAL_GROUP)
                        .set(EducationalGroup.EDUCATIONAL_GROUP.GROUP_ID, educationalGroup.getGroupId())
                        .set(EducationalGroup.EDUCATIONAL_GROUP.NAME, educationalGroup.getName())
                        .set(EducationalGroup.EDUCATIONAL_GROUP.COURSE, educationalGroup.getCourse())
                        .set(EducationalGroup.EDUCATIONAL_GROUP.FACULTY_ID, educationalGroup.getFacultyId())
                        .execute();
            } catch (Exception e) {
                log.debug("Error with saving group {}", e.getMessage());
            }
        }
    }

    public List<EducationalGroupRecord> findAllByFacultyIdAndCourse(Long facultyId, Long course) {
        return dslContext.select()
                .from(EducationalGroup.EDUCATIONAL_GROUP)
                .where(EducationalGroup.EDUCATIONAL_GROUP.FACULTY_ID.eq(facultyId).and(EducationalGroup.EDUCATIONAL_GROUP.COURSE.eq(course)))
                .fetchInto(EducationalGroupRecord.class);
    }
}
