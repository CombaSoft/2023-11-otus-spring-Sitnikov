package ru.otus.hw.config;

import java.util.List;
import java.util.Locale;

public interface LocalePropertiesProvider {

    List<Locale> getAvailableLocalesProperty();

    Locale getDefaultLocaleProperty();
}
