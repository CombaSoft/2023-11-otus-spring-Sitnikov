package ru.otus.hw.service.locale;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocalePropertiesProvider;

import java.util.Locale;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService, LocaleKeeper {

    private final MessageSource messageSource;

    private Optional<Locale> locale = Optional.empty();

    private final LocalePropertiesProvider propertiesProvider;

    public String getLocalizedMessage(String messageCode, String... args) {

        return messageSource.getMessage(messageCode, args, getLocale());
    }

    public Locale getLocale() {
        if (locale.isEmpty()) {
            locale = Optional.ofNullable(propertiesProvider.getDefaultLocaleProperty());
        }
        return locale.get();
    }

    public void setLocale(Locale locale) {
        this.locale = Optional.of(locale);
    }
}
