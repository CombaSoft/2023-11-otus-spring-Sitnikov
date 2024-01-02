package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.service.locale.LocaleKeeper;

@RequiredArgsConstructor
@Service
public class FileNameProvider implements TestFileNameProvider {

    private final TestConfig testConfig;


    private final LocaleKeeper localeKeeper;

    @Override
    public String getTestFileName() {
        return testConfig.getFileNameByLocale(localeKeeper.getLocale());
    }
}
