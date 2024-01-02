package ru.otus.hw.commandlinerunners;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TestRunner.class);

    private final TestRunnerService testRunnerService;

    @Override
    public void run(String[] args) {
        LOG.info("Start");
        testRunnerService.run();
        LOG.info("End");
    }
}
