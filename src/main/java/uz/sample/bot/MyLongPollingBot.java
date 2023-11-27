package uz.sample.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
public class MyLongPollingBot extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String botUsername;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MyLongPollingBot(ApplicationEventPublisher applicationEventPublisher,
                            @Value("${bot.token}") String botToken) {
        super(botToken);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        applicationEventPublisher.publishEvent(update);
    }

    @EventListener
    public void listenForBotApiMethod(BotApiMethod<?> b) {
        try {
            execute(b);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
        }
    }
}
