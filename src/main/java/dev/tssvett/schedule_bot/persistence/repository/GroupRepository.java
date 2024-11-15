package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.Tables;
import dev.tssvett.schedule_bot.persistence.model.tables.EducationalGroup;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                dslContext.insertInto(Tables.EDUCATIONAL_GROUP)
                        .columns(
                                Tables.EDUCATIONAL_GROUP.GROUP_ID,
                                Tables.EDUCATIONAL_GROUP.NAME,
                                Tables.EDUCATIONAL_GROUP.COURSE,
                                Tables.EDUCATIONAL_GROUP.FACULTY_ID
                        )
                        .values(
                                educationalGroup.getGroupId(),
                                educationalGroup.getName(),
                                educationalGroup.getCourse(),
                                educationalGroup.getFacultyId()
                        )
                        .execute();
            } catch (IntegrityConstraintViolationException e) {
                log.warn("Group with id {} already exists", educationalGroup.getGroupId());
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
