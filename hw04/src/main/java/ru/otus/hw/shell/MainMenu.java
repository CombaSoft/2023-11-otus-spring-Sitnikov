package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

import java.util.Objects;

@ShellComponent
@RequiredArgsConstructor
public class MainMenu {


    private final StudentService studentService;


    private final TestService testService;


    private final ResultService resultService;

    private Student student = null;

    private TestResult testResult = null;


    @ShellMethod(value = "Define student", key = {"ds", "l", "login"})
    public void defineStudent() {
        student = studentService.determineCurrentStudent();
    }

    @ShellMethod(value = "Proceed test", key = {"pt", "t", "test"})
    public void proceedTest() {
        testResult = testService.executeTestFor(student);
    }

    @ShellMethod(value = "Show result", key = {"sr", "r", "result"})
    public void showResult() {
        resultService.showResult(testResult);
    }

    public Availability proceedTestAvailability() {
        return Objects.nonNull(student)
                ? Availability.available()
                : Availability.unavailable("you are not defined student");
    }

    public Availability showResultAvailability() {
        return Objects.nonNull(testResult)
                ? Availability.available()
                : Availability.unavailable("you haven't finished the test");
    }
}