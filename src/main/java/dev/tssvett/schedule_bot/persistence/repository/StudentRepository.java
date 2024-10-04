package dev.tssvett.schedule_bot.persistence.repository;

import dev.tssvett.schedule_bot.persistence.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
