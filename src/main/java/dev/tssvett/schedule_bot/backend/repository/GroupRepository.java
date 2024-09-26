package dev.tssvett.schedule_bot.backend.repository;

import dev.tssvett.schedule_bot.backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
