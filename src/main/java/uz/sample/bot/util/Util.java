package uz.sample.bot.util;

import uz.sample.bot.entity.BotSession;
import uz.sample.bot.exception.ValidationException;

import java.util.Objects;

public class Util {

    public static void checkBotSessionForUntilThePageForNull(BotSession session) throws ValidationException {
        if (Objects.isNull(session))
            throw ValidationException.me("BotSession is null");
        if (Objects.isNull(session.getState()))
            throw ValidationException.me("State is null");
        if (Objects.isNull(session.getState().getPage()))
            throw ValidationException.me("Page is null");
        if (Objects.isNull(session.getState().getName()))
            throw ValidationException.me("StateName is null");
    }
}
