package ru.otus.hw.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.locale.LocalizationService;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Class TestServiceImpl")
public class TestServiceImplTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Mock
    private QuestionPresenterService presenterService;

    @Mock
    private IOService ioService;
    @Mock
    private Student student;

    private QuestionDao questionDao;

    private TestService testService;

    @Mock
    private LocalizationService localizationService;


    @BeforeEach
    void setUp() {
        questionDao = new CsvQuestionDao(fileNameProvider);
        testService = new TestServiceImpl(ioService, questionDao, presenterService, localizationService);
    }

    @Order(1)
    @DisplayName("We can pass test using correct answers")
    @Test
    void weShouldPassTestUsingCorrectAnswers() {

        given(fileNameProvider.getTestFileName()).willReturn("test_questions.csv");
        given(presenterService.getPresentation(any())).willReturn("");
        given(localizationService.getLocalizedMessage(any())).willReturn("");

        given(ioService.readIntForRangeWithPrompt(Mockito.eq(1), Mockito.eq(2),
                Mockito.anyString(),
                Mockito.anyString()))
                .willReturn(2);

        given(ioService.readIntForRangeWithPrompt(Mockito.eq(1), Mockito.eq(3),
                Mockito.anyString(),
                Mockito.anyString()))
                .willReturn(3);

        TestResult testResult = testService.executeTestFor(student);

        Assert.isTrue(Integer.valueOf(3).equals(testResult.getRightAnswersCount()), "Incorrect test answers sequence!");

    }

    @Order(2)
    @DisplayName(".. and we should not pass test using incorrect answers")
    @Test
    void weShouldNotPassTestUsingIncorrectAnswers() {
        given(fileNameProvider.getTestFileName()).willReturn("test_questions.csv");
        given(presenterService.getPresentation(any())).willReturn("");
        given(localizationService.getLocalizedMessage(Mockito.anyString())).willReturn("");

        given(ioService.readIntForRangeWithPrompt(Mockito.eq(1), Mockito.eq(2),
                Mockito.anyString(),
                Mockito.anyString()))
                .willReturn(1);

        given(ioService.readIntForRangeWithPrompt(Mockito.eq(1), Mockito.eq(3),
                Mockito.anyString(),
                Mockito.anyString()))
                .willReturn(2);

        TestResult testResult = testService.executeTestFor(student);

        Assert.isTrue(Integer.valueOf(0).equals(testResult.getRightAnswersCount()), "Incorrect test answers sequence!");
    }
}
