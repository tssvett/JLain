package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.model.tables.Student;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepository {
    private final DSLContext dslContext;

    public List<StudentRecord> findAll() {
        return dslContext.select()
                .from(Student.STUDENT)
                .fetchInto(StudentRecord.class);
    }

    public Optional<StudentRecord> findById(Long studentId) {
        return dslContext.select()
                .from(Student.STUDENT)
                .where(Student.STUDENT.USER_ID.eq(studentId))
                .fetchOptionalInto(StudentRecord.class);
    }

    public void save(StudentRecord student) {
        dslContext.insertInto(Student.STUDENT)
                .set(Student.STUDENT.USER_ID, student.getUserId())
                .set(Student.STUDENT.CHAT_ID, student.getChatId())
                .set(Student.STUDENT.COURSE, student.getCourse())
                .set(Student.STUDENT.REGISTRATION_STATE, student.getRegistrationState())
                .set(Student.STUDENT.FACULTY_ID, student.getFacultyId())
                .set(Student.STUDENT.GROUP_ID, student.getGroupId())
                .execute();
    }

    public void updateAll(Long studentId, StudentRecord student) {
        dslContext.update(Student.STUDENT)
                .set(Student.STUDENT.USER_ID, student.getUserId())
                .set(Student.STUDENT.CHAT_ID, student.getChatId())
                .set(Student.STUDENT.COURSE, student.getCourse())
                .set(Student.STUDENT.REGISTRATION_STATE, student.getRegistrationState())
                .set(Student.STUDENT.FACULTY_ID, student.getFacultyId())
                .set(Student.STUDENT.GROUP_ID, student.getGroupId())
                .where(Student.STUDENT.USER_ID.eq(studentId))
                .execute();
    }

    public void updateState(Long studentId, String state) {
        dslContext.update(Student.STUDENT)
                .set(Student.STUDENT.REGISTRATION_STATE, state)
                .where(Student.STUDENT.USER_ID.eq(studentId))
                .execute();
    }
}
