package ru.otus.hw.service.locale;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocaleConfig;

@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private final MessageSource messageSource;

    private final LocaleConfig localeConfig;

    public String getLocalizedMessage(String messageCode, String... args) {

        return messageSource.getMessage(messageCode, args, localeConfig.getLocale());
    }
}
