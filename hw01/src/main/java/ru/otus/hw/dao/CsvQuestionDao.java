package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final int NUMBER_OF_SKIPPED_LINES = 1;

    private static final char COMMA_SEPARATOR = ';';

    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        List<QuestionDto> beans = new ArrayList<>();

        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
             Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {

            beans.addAll(new CsvToBeanBuilder(reader)
                    .withType(QuestionDto.class).
                    withSkipLines(NUMBER_OF_SKIPPED_LINES).
                    withSeparator(COMMA_SEPARATOR).
                    build().parse());

        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e.getCause());
        }

        return beans.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
    }
}
