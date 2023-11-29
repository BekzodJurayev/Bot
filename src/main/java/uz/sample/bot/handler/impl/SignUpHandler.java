package uz.sample.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.sample.bot.constant.CallbackDataName;
import uz.sample.bot.handler.UpdateHandler;
import uz.sample.bot.model.BotUpdate;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.service.BotSessionService;
import uz.sample.bot.service.StateService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SignUpHandler implements UpdateHandler {
    private static final Logger _logger = LoggerFactory.getLogger(SignUpHandler.class);
    private static final String LANDING_STATE_NAME = "";
    private static final String NAME_EXPECTED = "name input expected";

    private final StateService stateService;
    private final BotSessionService sessionService;
    private final ApplicationEventPublisher publisher;

    @Override
    public Page getPage() {
        return Page.SIGN_UP;
    }

    @Override
    public String getLandingStateName() {
        return LANDING_STATE_NAME;
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of(
                LANDING_STATE_NAME,
                NAME_EXPECTED
        );
    }

    @Override
    public BotApiMethod<?> process(@NonNull BotUpdate update) {
        try {
            switch (update.getSession().getState().getName()) {
                case LANDING_STATE_NAME -> {
                    return askName(update);
                }
                case NAME_EXPECTED -> {
                    return setNameAndAskLoginInput(update);
                }
            }
        } catch (Exception e) {
            _logger.error(e.getMessage());
        }
        return null;
    }

    private SendMessage askName(BotUpdate update) {
        SendMessage msg = new SendMessage(update.getSession().getChatId().toString(), "Please input your name.");
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("continue as %s %s".formatted(update.getSession().getFirstName(), StringUtils.getIfEmpty(update.getSession().getLastName(), () -> "")));
        button.setCallbackData(CallbackDataName.CONTINUE_AS_CURRENT_NAME);
        msg.setReplyMarkup(new InlineKeyboardMarkup(List.of(List.of(button))));
        update.getSession().setState(stateService.findStateByPageAndName(getPage(), NAME_EXPECTED));
        sessionService.saveBotSession(update.getSession());
        return msg;
    }
    private SendMessage setNameAndAskLoginInput(BotUpdate update) {
        Integer messageId;
        if (Objects.nonNull(update.getCallBackQuery())) {
            messageId = update.getCallBackQuery().getMessageId();
        } else
            messageId = update.getMessage().getId();
        publisher.publishEvent(new DeleteMessage(update.getSession().getChatId().toString(), messageId));
        return null;
    }
}
