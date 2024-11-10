package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.tables.Faculty;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
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
public class FacultyRepository {
    private final DSLContext dslContext;

    public List<FacultyRecord> findAll() {
        return dslContext.select()
                .from(Faculty.FACULTY)
                .fetchInto(FacultyRecord.class);
    }

    public void saveAll(List<FacultyRecord> faculties) {

        for (FacultyRecord faculty : faculties) {
            try {
                dslContext.insertInto(Faculty.FACULTY)
                        .columns(Faculty.FACULTY.FACULTY_ID, Faculty.FACULTY.NAME)
                        .values(faculty.getFacultyId(), faculty.getName())
                        .execute();
            }
            catch (IntegrityConstraintViolationException e){
                log.warn("Faculty with id {} already exists", faculty.getFacultyId());
            }
        }
    }

    public Optional<FacultyRecord> findById(Long facultyId) {
        return dslContext.selectFrom(Faculty.FACULTY)
                .where(Faculty.FACULTY.FACULTY_ID.eq(facultyId))
                .fetchOptional();
    }
}
