package uz.sample.bot.service;

import org.springframework.lang.NonNull;
import uz.sample.bot.entity.BotSession;

public interface HistoryService {
    void considerSession(@NonNull BotSession session);
}
