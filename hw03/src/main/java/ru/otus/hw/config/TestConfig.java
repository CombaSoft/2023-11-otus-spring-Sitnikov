package ru.otus.hw.config;

import java.util.Locale;

public interface TestConfig {

    int getRightAnswersCountToPass();

    String getFileNameByLocale(Locale locale);
}
