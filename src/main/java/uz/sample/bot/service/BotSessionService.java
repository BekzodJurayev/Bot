package uz.sample.bot.service;

import org.springframework.lang.NonNull;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.sample.bot.entity.BotSession;

public interface BotSessionService {
    BotSession getOrCreateBotSession(@NonNull User user, Long chatId);
//    BotSession getBotSessionByChatId(@NonNull Long chatId);
//    BotSession getBotSessionByUserId(@NonNull Long userId);
    void saveBotSession(@NonNull BotSession session);
}
