package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.tables.Faculty;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
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
                        .set(Faculty.FACULTY.FACULTY_ID, faculty.getFacultyId())
                        .set(Faculty.FACULTY.NAME, faculty.getName())
                        .onDuplicateKeyUpdate()
                        .set(Faculty.FACULTY.NAME, faculty.getName())
                        .execute();
            } catch (DuplicateKeyException e) {
                log.debug("Faculty with id {} already exists", faculty.getFacultyId());
            }
        }
    }

    public Optional<FacultyRecord> findById(Long facultyId) {
        return dslContext.selectFrom(Faculty.FACULTY)
                .where(Faculty.FACULTY.FACULTY_ID.eq(facultyId))
                .fetchOptional();
    }
}
