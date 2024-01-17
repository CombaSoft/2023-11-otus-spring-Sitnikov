package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Book> result =
                jdbcTemplate.query(
                        """
                                select book.id book_id, book.title book_title,\s
                                       author.id author_id, author.full_name author_full_name,\s
                                       genre.id genre_id, genre.name genre_name\s
                                from books book, authors author, genres genre\s
                                where author.id = book.author_id\s
                                  and genre.id = book.genre_id\s
                                  and book.id = :id\s""",
                        params, new BookRowMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.iterator().next());
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(
                """
                        select book.id book_id, book.title book_title,\s
                               author.id author_id, author.full_name author_full_name,\s
                               genre.id genre_id, genre.name genre_name\s
                        from books book, authors author, genres genre\s
                        where author.id = book.author_id\s
                          and genre.id = book.genre_id\s""",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {

        Map<String, Object> queryMapParameters = Map.of("id", id);

        SqlParameterSource sqlParameters = new MapSqlParameterSource(queryMapParameters);

        jdbcTemplate.update("delete from books where id = :id", sqlParameters);
    }

    private Book insert(Book book) {

        Map<String, Object> queryMapParameters = Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId(),
                "id", (long) -1);

        SqlParameterSource sqlParameters = new MapSqlParameterSource(queryMapParameters);

        var keyHolder = new GeneratedKeyHolder();

        //noinspection DataFlowIssue
        jdbcTemplate.update(getMergeQuery(), sqlParameters, keyHolder);

        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {

        Map<String, Object> queryMapParameters = Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId());
        SqlParameterSource sqlParameters = new MapSqlParameterSource(queryMapParameters);

        int countOfUpdatedEntities =
                jdbcTemplate.update(getMergeQuery(), sqlParameters);

        if (countOfUpdatedEntities < 1) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }
        return book;
    }

    private String getMergeQuery() {
        return """
         merge into books t\s
         using\s
         (
           select
              CAST(:id AS INTEGER) as id,
              CAST(:title AS VARCHAR) as title,
              CAST(:author_id AS INTEGER) as author_id,
              CAST(:genre_id AS INTEGER) as genre_id
           from dual
         ) v\s
         on
         (
           t.id = v.id
         )
         when matched then update set title = v.title, author_id = v.author_id, genre_id = v.genre_id
         when not matched then insert (title, author_id, genre_id)
         values (v.title, v.author_id, v.genre_id)""";
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            String bookTitle = rs.getString("book_title");

            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("author_full_name");

            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");

            Author author = new Author(authorId, authorFullName);
            Genre genre = new Genre(genreId, genreName);

            return new Book(bookId, bookTitle, author, genre);
        }
    }
}
