package ru.otus.hw.service.locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import ru.otus.hw.Application;
import ru.otus.hw.shell.MainMenu;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@ComponentScan({"ru.otus.hw.config", "ru.otus.hw.service.locale", "ru.otus.hw.service.io"})
@DisplayName("LocalizationServiceImpl")
public class LocalizationServiceImplTest {


    @Autowired
    LocalizationService localizationService;

    @MockBean
    MainMenu mainMenu;

    @Order(1)
    @DisplayName("Localization service should use message source properly in case of en-US locale")
    @Test
    void localizationServiceWorksCorrectlyFor_ru_RU_locale() {

        String expectedResult = "Enter your first name";

        String actualResult = localizationService.getLocalizedMessage("input.first.name");

        Assert.isTrue(expectedResult.equals(actualResult), "LocalizationService returns incorrect result for ru-RU locale");
    }
}
