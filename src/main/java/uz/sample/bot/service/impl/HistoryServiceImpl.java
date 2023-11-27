package uz.sample.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import uz.sample.bot.entity.BotSession;
import uz.sample.bot.repository.HistoryRepository;
import uz.sample.bot.service.HistoryService;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    @Override
    public void considerSession(@NonNull BotSession session) {

    }
}
