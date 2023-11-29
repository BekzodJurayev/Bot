package uz.sample.bot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long chatId;

    @ManyToOne(optional = false)
    private State state;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    private String userName;

    @ManyToOne
    private Account account;
}
