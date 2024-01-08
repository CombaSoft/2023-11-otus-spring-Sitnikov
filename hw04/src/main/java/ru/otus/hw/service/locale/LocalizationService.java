package ru.otus.hw.service.locale;

public interface LocalizationService {
    String getLocalizedMessage(String messageCode, String... args);
}
