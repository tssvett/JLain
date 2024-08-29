package dev.tssvett.schedule_bot.repository;

import dev.tssvett.schedule_bot.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
