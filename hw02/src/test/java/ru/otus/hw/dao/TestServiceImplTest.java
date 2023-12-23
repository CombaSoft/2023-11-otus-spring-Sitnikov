package ru.otus.hw.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.QuestionPresenterService;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;


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


    @BeforeEach
    void setUp() {
        questionDao = new CsvQuestionDao(fileNameProvider);
        testService = new TestServiceImpl(ioService, questionDao, presenterService);
    }

    @Order(1)
    @DisplayName("We can pass test using correct answers")
    @Test
    void weShouldPassTestUsingCorrectAnswers() {
        given(fileNameProvider.getTestFileName()).willReturn("test_questions.csv");
        given(presenterService.getPresentation(any())).willReturn("");

        given(ioService.readIntForRangeWithPrompt(1, 2,
                "Please choose answer number(it should be in range [1..2] ): ",
                "Wrong number format or number is out of bounds [1..2]"))
                .willReturn(2);

        given(ioService.readIntForRangeWithPrompt(1, 3,
                "Please choose answer number(it should be in range [1..3] ): ",
                "Wrong number format or number is out of bounds [1..3]"))
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

        given(ioService.readIntForRangeWithPrompt(1, 2,
                "Please choose answer number(it should be in range [1..2] ): ",
                "Wrong number format or number is out of bounds [1..2]"))
                .willReturn(1);

        given(ioService.readIntForRangeWithPrompt(1, 3,
                "Please choose answer number(it should be in range [1..3] ): ",
                "Wrong number format or number is out of bounds [1..3]"))
                .willReturn(2);

        TestResult testResult = testService.executeTestFor(student);

        Assert.isTrue(Integer.valueOf(0).equals(testResult.getRightAnswersCount()), "Incorrect test answers sequence!");
    }

}
