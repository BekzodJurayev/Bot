package uz.sample.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import uz.sample.bot.entity.State;
import uz.sample.bot.model.enums.Page;
import uz.sample.bot.repository.StateRepository;
import uz.sample.bot.service.StateService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    private final StateRepository stateRepository;

    @Override
    public void saveIfNotExist(@NonNull Page page, @NonNull String name) {
        stateRepository.findByPageAndName(page, name)
                .orElseGet(() -> stateRepository.save(State.builder()
                        .name(name)
                        .page(page)
                        .landing(false)
                        .build()));
    }

    @Override
    public void saveLandingState(@NonNull Page page, @NonNull String name) {
        State state = stateRepository.findByPageAndName(page, name)
                .orElseGet(()->State.builder()
                        .name(name)
                        .page(page)
                        .landing(true)
                        .build());
        state.setLanding(true);
        stateRepository.save(state);
    }

    @Override
    public void removeStatesByPageAndNameNotIn(@NonNull Page page, @NonNull Set<String> names) {
        stateRepository.removeStatesByPageAndNameNotIn(page, names);
    }

    @Override
    public State findLandingStateByPage(@NonNull Page page) {
        return stateRepository.findByPageAndLandingIsTrue(page);
    }

    @Override
    public State findStateByPageAndName(@NonNull Page page, @NonNull String name) {
        return stateRepository.findByPageAndName(page, name).orElseThrow();
    }

}
