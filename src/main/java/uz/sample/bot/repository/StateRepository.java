package uz.sample.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import uz.sample.bot.entity.State;
import uz.sample.bot.model.enums.Page;

import java.util.Optional;
import java.util.Set;

public interface StateRepository extends JpaRepository<State, Integer> {
    Optional<State> findByPageAndName(@NonNull Page page, @NonNull String name);
    void removeStatesByPageAndNameNotIn(@NonNull Page page, @NonNull Set<String> name);

    State findByPageAndLandingIsTrue(@NonNull Page page);
}
