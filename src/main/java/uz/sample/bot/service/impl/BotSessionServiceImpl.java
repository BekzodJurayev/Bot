package uz.sample.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.sample.bot.entity.BotSession;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.repository.BotSessionRepository;
import uz.sample.bot.service.BotSessionService;
import uz.sample.bot.service.HistoryService;
import uz.sample.bot.service.StateService;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotSessionServiceImpl implements BotSessionService {
    private final BotSessionRepository sessionRepository;
    private final HistoryService historyService;
    private final StateService stateService;

    @Override
    public BotSession getOrCreateBotSession(@NonNull User user, Long chatId) {
        BotSession session = sessionRepository.findByUserId(user.getId())
                .orElseGet(() -> Objects.isNull(chatId) ? null : new BotSession());
        if (Objects.isNull(session))
            return null;
        if (Objects.isNull(session.getId())) {
            session.setUserId(user.getId());
            session.setChatId(chatId);
            session.setState(stateService.findLandingStateByPage(Page.HOME));
        }
        session.setFirstName(user.getFirstName());
        session.setLastName(user.getLastName());
        session.setUserName(user.getUserName());
        sessionRepository.save(session);
        return session;
    }

//    @Override
    public BotSession getBotSessionByChatId(@NonNull Long chatId) {
        Optional<BotSession> optional = sessionRepository.findByChatId(chatId);
        BotSession session = null;
        if (optional.isPresent())
            session = optional.get();
        return session;
    }

//    @Override
    public BotSession getBotSessionByUserId(@NonNull Long userId) {
        Optional<BotSession> optional = sessionRepository.findByUserId(userId);
        BotSession session = null;
        if (optional.isPresent())
            session = optional.get();
        return session;
    }

    @Override
    public void saveBotSession(@NonNull BotSession session) {
        historyService.considerSession(session);
        sessionRepository.save(session);
    }
}
