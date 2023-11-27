package uz.sample.bot.service;

import org.springframework.lang.NonNull;
import uz.sample.bot.entity.State;
import uz.sample.bot.model.enums.Page;

import java.util.Set;

public interface StateService {
    void saveIfNotExist(@NonNull Page page, @NonNull String name);

    void saveLandingState(@NonNull Page page, @NonNull String name);

    void removeStatesByPageAndNameNotIn(@NonNull Page page, @NonNull Set<String> names);

    State findLandingStateByPage(@NonNull Page page);

    State findStateByPageAndName(@NonNull Page page, @NonNull String name);
}
