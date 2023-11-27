package uz.sample.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private State state;

    @Column(nullable = false)
    private boolean current;

    @OneToOne(fetch = FetchType.LAZY)
    private History prev;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private BotSession session;
}
