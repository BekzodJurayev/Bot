package uz.sample.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.sample.bot.constant.KeyboardButtonName;
import uz.sample.bot.entity.BotSession;
import uz.sample.bot.entity.State;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.handler.UpdateHandler;
import uz.sample.bot.model.BotUpdate;
import uz.sample.bot.service.BotSessionService;
import uz.sample.bot.service.StateService;
import uz.sample.bot.util.Util;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LandingHandler implements UpdateHandler {
    private static final Logger _logger = LoggerFactory.getLogger(LandingHandler.class);
    private static final String LANDING_STATE_NAME = "";
    private static final String SIGN_EXPECTING = "sign";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final BotSessionService sessionService;
    private final StateService stateService;

    @Override
    public Page getPage() {
        return Page.HOME;
    }

    @Override
    public String getLandingStateName() {
        return LANDING_STATE_NAME;
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of(
                LANDING_STATE_NAME,
                SIGN_EXPECTING
        );
    }

    @Override
    public BotApiMethod<?> process(@NonNull BotUpdate update) {
        return processInternal(update);
    }

    private BotApiMethod<?> processInternal(@NonNull BotUpdate update) {
        try {
            Util.checkBotSessionForUntilThePageForNull(update.getSession());
            switch (update.getSession().getState().getName()) {
                case LANDING_STATE_NAME -> {
                    return handleNewState(update.getSession());
                }
                case SIGN_EXPECTING -> handleSignExpectedState(update);
                default -> throw new RuntimeException("State is undefined");
            }
        } catch (Exception e) {
            _logger.error(e.getMessage());
        }
        return null;
    }

    private SendMessage handleNewState(BotSession session) {
        String text = "Hi bro.  What's up ? \nWelcome to my chat ! 😎";
        SendMessage msg = new SendMessage(session.getChatId().toString(), text);
        msg.setReplyMarkup(singOptionsKeyboardMarkup());
        session.setState(stateService.findStateByPageAndName(getPage(), SIGN_EXPECTING));
        sessionService.saveBotSession(session);
        return msg;
    }

    private void handleSignExpectedState(@NonNull BotUpdate update) {
        if (Objects.isNull(update.getMessage()))
            return;
        Integer messageId = update.getMessage().getId();
        String txt = update.getMessage().getText();
        if (Objects.nonNull(txt))
            switch (txt) {
                case KeyboardButtonName.SING_IN -> {
                    State signInState = stateService.findLandingStateByPage(Page.SIGN_IN);
                    update.getSession().setState(signInState);
                    sessionService.saveBotSession(update.getSession());
                    applicationEventPublisher.publishEvent(update);
                    applicationEventPublisher.publishEvent(new DeleteMessage(update.getSession().getChatId().toString(), messageId));
                }
                case KeyboardButtonName.SING_UP -> {
                    State signUpState = stateService.findLandingStateByPage(Page.SIGN_UP);
                    update.getSession().setState(signUpState);
                    sessionService.saveBotSession(update.getSession());
                    applicationEventPublisher.publishEvent(update);
                    applicationEventPublisher.publishEvent(new DeleteMessage(update.getSession().getChatId().toString(), messageId));
                }
            }
    }

    private ReplyKeyboardMarkup singOptionsKeyboardMarkup() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(List.of(
                new KeyboardRow(List.of(
                        new KeyboardButton(KeyboardButtonName.SING_IN),
                        new KeyboardButton(KeyboardButtonName.SING_UP)
                ))
        ));
        markup.setOneTimeKeyboard(true);
        return markup;
    }
}
