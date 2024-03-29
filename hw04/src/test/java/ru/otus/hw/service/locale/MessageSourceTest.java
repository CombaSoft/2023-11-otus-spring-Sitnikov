package ru.otus.hw.service.locale;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import ru.otus.hw.Application;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.shell.MainMenu;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@ComponentScan({"ru.otus.hw.config", "ru.otus.hw.service.locale", "ru.otus.hw.service.io"})
@DisplayName("MessageSource")
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Autowired
    LocaleConfig localeConfig;

    @MockBean
    MainMenu mainMenu;

    @Order(1)
    @DisplayName("MessageSource returns correct message")
    @Test
    void messageSourcePositiveTest() {

        String expectedResult = "Enter your first name";

        String actualResult = messageSource.getMessage("input.first.name", null, localeConfig.getLocale());

        Assert.isTrue(expectedResult.equals(actualResult), "MessageSource returns incorrect result for en-US locale");
    }

    @Order(2)
    @DisplayName("MessageSource returns throws NoSuchMessageException")
    @Test
    void messageSourceNegativeTest() {

        Exception thrown = assertThrows(
                NoSuchMessageException.class,
                () -> {
                    messageSource.getMessage("input.first.name_", null, localeConfig.getLocale());
                },
                "Expected messageSource.getMessage() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("No message found under code"));
    }
}
