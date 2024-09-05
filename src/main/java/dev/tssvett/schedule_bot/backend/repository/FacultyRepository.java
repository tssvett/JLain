package dev.tssvett.schedule_bot.backend.repository;

import dev.tssvett.schedule_bot.backend.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
