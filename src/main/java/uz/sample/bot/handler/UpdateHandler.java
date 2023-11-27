package uz.sample.bot.handler;

import org.springframework.lang.NonNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.model.BotUpdate;

import java.util.Set;

public interface UpdateHandler {
    Page getPage();
    String getLandingStateName();
    Set<String> getStateNames();
    BotApiMethod<?> process(@NonNull BotUpdate update);
}
