package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.locale.LocalizationService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {


    private final TestConfig testConfig;

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLine(localizationService.getLocalizedMessage("test.results"));
        ioService.printFormattedLine(localizationService.getLocalizedMessage("student.s"),
                testResult.getStudent().getFullName());
        ioService.printFormattedLine(localizationService.getLocalizedMessage("answ.q.cnt.d"),
                testResult.getAnsweredQuestions().size());
        ioService.printFormattedLine(localizationService.getLocalizedMessage("right.answ.cnt.d"),
                testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine(localizationService.getLocalizedMessage("test.results.passed"));
            return;
        }
        ioService.printLine(localizationService.getLocalizedMessage("test.results.fail"));
    }
}
