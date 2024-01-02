package ru.otus.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "test")
public class TestProperties implements TestConfig {

    private final Map<Locale, String> fileNames;

    private final int rightAnswersCountToPass;

    @ConstructorBinding
    public TestProperties(Map<Locale, String> fileNames, int rightAnswersCountToPass) {
        this.fileNames = fileNames;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }

    @Override
    public String getFileNameByLocale(Locale locale) {
        return fileNames.get(locale);
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }
}