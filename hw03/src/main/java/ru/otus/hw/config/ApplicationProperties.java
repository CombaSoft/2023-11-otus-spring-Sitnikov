package ru.otus.hw.config;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties implements LocalePropertiesProvider {

    private Locale defaultLocale;

    private final List<Locale> availableLocales;

    @ConstructorBinding
    public ApplicationProperties(Locale defaultLocale, List<Locale> availableLocales) {
        this.availableLocales = availableLocales;
        this.defaultLocale = defaultLocale;
    }

    @Override
    public List<Locale> getAvailableLocalesProperty() {
        return List.copyOf(availableLocales);
    }

    @Override
    public Locale getDefaultLocaleProperty() {
        return defaultLocale;
    }
}
