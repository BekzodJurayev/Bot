package uz.sample.bot.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotMessage {
    private Integer id;
    private String text;
}
