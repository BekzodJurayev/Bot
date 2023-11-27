package uz.sample.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import uz.sample.bot.service.HistoryService;
import uz.sample.bot.service.StateService;
import uz.sample.bot.util.Util;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HomePageHandler implements UpdateHandler {
    private static final String LANDING_STATE_NAME = "";
    private static final String SIGN = "sign";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final BotSessionService sessionService;
    private final StateService stateService;
    private final HistoryService historyService;

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
                SIGN
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
                case SIGN -> {
                    handleSignExpectedState(update);
                    return null;
                }
                default -> throw new RuntimeException("State is undefined");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private SendMessage handleNewState(BotSession session) {
        String text = "Hi bro.  What's up ? \nWelcome to my chat ! 😎";
        SendMessage msg = new SendMessage(session.getChatId().toString(), text);
        ReplyKeyboardMarkup markup =  new ReplyKeyboardMarkup(List.of(singOptionsKeyboardRow()));
        markup.setOneTimeKeyboard(true);
        msg.setReplyMarkup(markup);
        session.setState(stateService.findStateByPageAndName(getPage(), SIGN));
        sessionService.saveBotSession(session);
        return msg;
    }

    private void handleSignExpectedState(@NonNull BotUpdate update) {
        if (Objects.isNull(update.getMessage()))
            return;
        String txt = update.getMessage().getText();
        if (Objects.nonNull(txt))
            switch (txt) {
                case KeyboardButtonName.SING_IN -> {
                    State signInState = stateService.findLandingStateByPage(Page.SIGN_IN);
                    update.getSession().setState(signInState);
                    sessionService.saveBotSession(update.getSession());
                    applicationEventPublisher.publishEvent(update);
                }
                case KeyboardButtonName.SING_UP -> {
                    State signUpState = stateService.findLandingStateByPage(Page.SIGN_UP);
                    update.getSession().setState(signUpState);
                    sessionService.saveBotSession(update.getSession());
                    applicationEventPublisher.publishEvent(update);
                }
            }
    }

    private KeyboardRow singOptionsKeyboardRow() {
        return new KeyboardRow(List.of(
                new KeyboardButton(KeyboardButtonName.SING_IN),
                new KeyboardButton(KeyboardButtonName.SING_UP)
        ));
    }
}
