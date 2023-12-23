package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final Logger LOG = LoggerFactory.getLogger(TestServiceImpl.class);

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionPresenterService presenterService;

    //todo: investigate why info from line 28 appeared in log file twice in about the same time.
    @Override
    public TestResult executeTestFor(Student student) {
        LOG.info("Start");
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (Question question : questions) {
            var isAnswerValid = askQuestionAndGetAnswer(question); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestionAndGetAnswer(Question question) {
        final int lowerBound = 1;
        int upperBound = question.answers().size();
        LOG.debug("upperBound {}", upperBound);
        ioService.printLine(presenterService.getPresentation(question));
        int userChoice = ioService.readIntForRangeWithPrompt(lowerBound,
                upperBound,
                "Please choose answer number(it should be in range [" + lowerBound + ".." + upperBound + "] ): ",
                "Wrong number format or number is out of bounds [1.." + upperBound + "]");
        ioService.printLine("");
        Answer answer = question.answers().get(userChoice - lowerBound);
        return answer.isCorrect();
    }
}
