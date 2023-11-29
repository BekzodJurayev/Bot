package uz.sample.bot.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotCallBackQuery {
    private Integer messageId;
    private String callbackData;
}
