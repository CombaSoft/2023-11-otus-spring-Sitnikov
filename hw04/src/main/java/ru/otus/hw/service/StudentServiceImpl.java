package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.locale.LocalizationService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(
                localizationService.getLocalizedMessage("input.first.name"));
        var lastName = ioService.readStringWithPrompt(
                localizationService.getLocalizedMessage("input.last.name"));
        return new Student(firstName, lastName);
    }
}
