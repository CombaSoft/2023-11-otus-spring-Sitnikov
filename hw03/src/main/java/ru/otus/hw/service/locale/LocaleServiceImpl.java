package ru.otus.hw.service.locale;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocalePropertiesProvider;
import ru.otus.hw.service.io.IOService;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private static final String CHOOSE_NUMBER_FROM_RANGE = "choose.number.from.range";

    private static final String WRONG_NUMBER = "wrong.number.format";


    private final IOService ioService;

    private final LocalizationService localizationService;

    private final LocalePropertiesProvider propertiesProvider;

    private final LocaleKeeper localeKeeper;

    @Override
    public void determineCurrentLocale() {

        localeKeeper.setLocale(getUserChoice());
    }

    private Locale getUserChoice() {

        printExistingLocales();

        int lowerBound = 1;
        int upperBound = propertiesProvider.getAvailableLocalesProperty().size();

        int userChoice = ioService.readIntForRangeWithPrompt(lowerBound,
                upperBound,
                localizationService.getLocalizedMessage(CHOOSE_NUMBER_FROM_RANGE) +
                        " [" + lowerBound + ".." + upperBound + "] ): ",
                localizationService.getLocalizedMessage(WRONG_NUMBER) +
                        " [1.." + upperBound + "]") - lowerBound;
        ioService.printLine("");


        return propertiesProvider.getAvailableLocalesProperty().get(userChoice);
    }

    private void printExistingLocales() {

        List<Locale> availableLocalesList = propertiesProvider.getAvailableLocalesProperty();

        ioService.printLine(localizationService.getLocalizedMessage("choice.locales"));

        for (int i = 0; i < availableLocalesList.size(); i++) {

            Locale locale = availableLocalesList.get(i);
            ioService.printLine("");
            ioService.printLine("[" + (i + 1) + "] " + locale.getDisplayName(localeKeeper.getLocale()));
        }
    }
}
