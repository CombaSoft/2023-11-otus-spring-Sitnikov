package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
public class QuestionPresenterServiceImpl implements QuestionPresenterService {

    private static final String SPACER = " ";

    private static final String NEW_LINE = "\n";

    @Override
    public String getPresentation(Question question) {
        StringBuilder result = new StringBuilder(question.text() + NEW_LINE);
        for (int i = 0; i < question.answers().size(); i = i + 1) {
            Answer answer = question.answers().get(i);
            result.append(i + 1).append(SPACER);
            result.append(answer.text()).append(SPACER);
            result.append(NEW_LINE);
        }
        return result.toString();
    }
}
