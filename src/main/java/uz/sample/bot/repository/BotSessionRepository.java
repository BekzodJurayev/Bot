package uz.sample.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sample.bot.entity.BotSession;

import java.util.Optional;

@Repository
public interface BotSessionRepository extends JpaRepository<BotSession, Integer> {
    Optional<BotSession> findByUserId(Long userId);
    Optional<BotSession> findByChatId(Long chatId);
}
