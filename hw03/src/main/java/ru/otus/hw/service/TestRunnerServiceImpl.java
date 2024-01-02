package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.locale.LocaleService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    private final LocaleService localeService;

    @Override
    public void run() {

        Exception e = null;

        try {
            localeService.determineCurrentLocale();
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (IllegalArgumentException exception) {
            System.out.println("User can`t enter correct value: " + exception.getMessage());
            e = exception;
        } catch (QuestionReadException exception) {
            System.out.println("Error during reading questions: " + exception.getMessage());
            e = exception;
        } catch (NoSuchMessageException exception) {
            System.out.println("Can`t get message for locale: " + exception.getMessage());
            e = exception;
        }

        if (Objects.nonNull(e)) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
