package dev.tssvett.schedule_bot.backend.repository;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<BotUser, Long> {
}
