package com.hsr.springai.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateExecutor implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        Map<String, UpdateRunner> beans = applicationContext.getBeansOfType(UpdateRunner.class);

        List<UpdateRunner> sortedRunners = new ArrayList<>(beans.values());
        sortedRunners.sort(Comparator.comparing(runner -> runner.getClass().getSimpleName()));

        log.info("Found {} update runners, executing in order...", sortedRunners.size());

        for (UpdateRunner runner : sortedRunners) {
            String runnerName = runner.getClass().getSimpleName();
            try {
                log.info("Executing update runner: {}", runnerName);
                runner.run();
            } catch (Exception e) {
                log.error("Failed to execute update runner: {}", runnerName, e);
            }
        }

        log.info("All update runners executed successfully");
    }
}
