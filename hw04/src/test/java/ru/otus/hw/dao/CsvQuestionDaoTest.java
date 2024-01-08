package ru.otus.hw.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Class CsvQuestionDao")
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        questionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Order(1)
    @DisplayName("Can be correctly created")
    @Test
    void sholudBeCorrectlyCreated() {
        Assert.notNull(questionDao, "Can`t create instance of CsvQuestionDao!");
    }

    @Order(2)
    @DisplayName("and findAll() returns non-empty result for existing resource")
    @Test
    void findAllSholudHaveNonEmptyResult() {
        given(fileNameProvider.getTestFileName()).willReturn("test_questions.csv");

        List<Question> questions = questionDao.findAll();
        Assert.notEmpty(questions, "findAll() returns empty result!");
    }

    @Order(3)
    @DisplayName("and findAll() generated QuestionReadException for non-existing resource")
    @Test
    void findAllFromNonExistingResourceSholudHaveEmptyResult() {
        given(fileNameProvider.getTestFileName()).willReturn("questions._csv");
        Assertions.assertThrows(QuestionReadException.class,
                () -> {
                    questionDao.findAll();
                },
                "QuestionReadException was expected!");
    }
    
}