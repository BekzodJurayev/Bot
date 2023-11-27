package uz.sample.bot.model;

import lombok.*;
import uz.sample.bot.entity.BotSession;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotUpdate {
    private BotSession session;
    private BotMessage message;
    private BotCallBackQuery callBackQuery;
}
