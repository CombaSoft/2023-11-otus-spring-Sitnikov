package ru.otus.hw.service.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class StreamsIOService implements IOService {

    private final Integer maxAttempts;

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamsIOService(@Value("${application.io.max_attempts:10}") int maxAttempts,
                            @Value("#{T(System).out}") PrintStream printStream,
                            @Value("#{T(System).in}") InputStream inputStream) {

        this.maxAttempts = maxAttempts;
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return scanner.nextLine();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < maxAttempts; i++) {
            try {
                var stringValue = scanner.nextLine();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}
