package uz.sample.bot.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.sample.bot.handler.UpdateHandler;
import uz.sample.bot.service.StateService;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final Set<UpdateHandler> handlers;
    private final StateService stateService;

    @Override
    public void run(String... args) {
        handlers.forEach(handler -> {
            handler.getStateNames().forEach(name -> stateService.saveIfNotExist(handler.getPage(), name));
            String landingStateName = handler.getLandingStateName();
            stateService.saveLandingState(handler.getPage(), landingStateName);
            stateService.removeStatesByPageAndNameNotIn(handler.getPage(), handler.getStateNames());
        });
    }
}
