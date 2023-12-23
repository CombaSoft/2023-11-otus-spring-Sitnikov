package ru.otus.hw.config;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Component
public class AppProperties implements TestFileNameProvider, TestConfig {

    @Value("${test.fileName:questions.csv}")
    private String testFileName;

    @Value("${test.rightAnswersCountToPass:3}")
    private int rightAnswersCountToPass;

    @Override
    public String getTestFileName() {

        return testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }
}
