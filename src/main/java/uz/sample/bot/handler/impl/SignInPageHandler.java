package uz.sample.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.sample.bot.handler.UpdateHandler;
import uz.sample.bot.model.BotUpdate;
import uz.sample.bot.model.enums.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SignInPageHandler implements UpdateHandler {
    private static final String LANDING_STATE_NAME = "";
    @Override
    public Page getPage() {
        return Page.SIGN_IN;
    }

    @Override
    public String getLandingStateName() {
        return LANDING_STATE_NAME;
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of(
                LANDING_STATE_NAME
        );
    }

    @Override
    public BotApiMethod<?> process(@NonNull BotUpdate update) {
        return new SendMessage(update.getSession().getChatId().toString(), "Sign in page");
    }
}
