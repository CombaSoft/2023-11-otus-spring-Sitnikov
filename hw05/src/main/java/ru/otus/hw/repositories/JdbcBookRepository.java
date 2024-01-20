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
        jdbcTemplate.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                sqlParameters, keyHolder);

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
                jdbcTemplate.update("""
                        update books\s
                        set\s
                          title = :title,\s
                          author_id = :author_id,\s
                          genre_id = :genre_id
                        where id = :id""", sqlParameters);

        if (countOfUpdatedEntities < 1) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }
        return book;
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
