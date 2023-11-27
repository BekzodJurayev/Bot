package uz.sample.bot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.sample.bot.model.enums.Page;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "page"})
})
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Page page;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean landing = false;

    @Override
    public int hashCode() {
        return (name + page.name()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof State s))
            return false;
        return Objects.equals(page, s.page) && Objects.equals(name, s.name);
    }

    @Override
    public String toString() {
        if (Objects.isNull(name) || Objects.isNull(page))
            return super.toString();
        return String.format("{\"page\": \"%s\", \"name\": \"%s\"}", page.name(), name);
    }
}
