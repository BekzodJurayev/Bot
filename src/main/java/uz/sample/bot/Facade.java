package uz.sample.bot;

import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.sample.bot.entity.BotSession;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.handler.UpdateHandler;
import uz.sample.bot.model.BotCallBackQuery;
import uz.sample.bot.model.BotMessage;
import uz.sample.bot.model.BotUpdate;
import uz.sample.bot.service.BotSessionService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Facade {
    private final ConcurrentHashMap<Page, UpdateHandler> handlers = new ConcurrentHashMap<>();
    private final BotSessionService botSessionService;

    public Facade(List<UpdateHandler> updateHandlers,
                  BotSessionService botSessionService) {
        if (Objects.nonNull(updateHandlers))
            updateHandlers.forEach(handler -> handlers.put(handler.getPage(), handler));
        this.botSessionService = botSessionService;
    }

    @EventListener
    public BotUpdate handleBotUpdate(@NonNull Update update) {
        BotSession session = findBotSession(update);

        BotMessage message = update.hasMessage() ? BotMessage.builder()
                .id(update.getMessage().getMessageId())
                .text(update.getMessage().getText())
                .build() : null;

        BotCallBackQuery callBackQuery = update.hasCallbackQuery() ? BotCallBackQuery.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .callbackData(update.getCallbackQuery().getData())
                .build() : null;

        return BotUpdate.builder()
                .session(session)
                .message(message)
                .callBackQuery(callBackQuery)
                .build();
    }

    @EventListener
    public BotApiMethod<?> handleBotUpdate(BotUpdate update) {
        if (Objects.isNull(update) || Objects.isNull(update.getSession()) || Objects.isNull(update.getSession().getState()))
            return null;
        UpdateHandler handler = handlers.get(update.getSession().getState().getPage());
        if (Objects.isNull(handler))
            return null;
        return handler.process(update);
    }

    private BotSession findBotSession(@NonNull Update update) {
        User user = getUser(update);
        if (Objects.isNull(user))
            return null;
        Long chatId = getChatId(update);
        return botSessionService.getOrCreateBotSession(user, chatId);
    }

    private User getUser(@NonNull Update update) {
        if (update.hasMessage())
            return update.getMessage().getFrom();
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom();
        System.err.println("Could not find user from the update id: " + update.getUpdateId());
        return null;
    }

    private Long getChatId(@NonNull Update update) {
        if (update.hasMessage())
            return update.getMessage().getChatId();
        else return null;
    }
}
