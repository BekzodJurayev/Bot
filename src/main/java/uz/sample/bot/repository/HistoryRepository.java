package uz.sample.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.sample.bot.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
