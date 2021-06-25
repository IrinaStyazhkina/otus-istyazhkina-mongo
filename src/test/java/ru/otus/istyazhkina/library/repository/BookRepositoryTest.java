package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.domain.entity.Book;
import ru.otus.istyazhkina.library.domain.entity.Comment;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.listener.MongoBookOperationsEventListener;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@DataMongoTest
@Import(MongoBookOperationsEventListener.class)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldReturnBookForExistingId() {
        Optional<Book> actualBook = bookRepository.findById("45632");
        assertThat(actualBook).isPresent().get()
                .matches(book -> book.getTitle().equals("War and Peace"))
                .matches(book -> book.getAuthor().equals(new Author("12345", "Lev", "Tolstoy")))
                .matches(book -> book.getGenre().equals(new Genre("2134", "novel")));
    }

    @Test
    void shouldReturnEmptyOptionalIfBookByIdNotExists() {
        Optional<Book> book = bookRepository.findById("10");
        assertThat(book).isEmpty();
    }

    @Test
    void shouldReturnBookForExistingTitle() {
        List<Book> books = bookRepository.findByTitle("War and Peace");
        assertThat(books).contains(new Book("45632", "War and Peace", new Author("12345", "Lev", "Tolstoy"), new Genre("2134", "novel")));
    }

    @Test
    void shouldReturnEmptyListIfBookByTitleNotExists() {
        assertThat(bookRepository.findByTitle("Unknown")).isEmpty();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        final List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewBookWithExistingAuthorAndGenre() {
        Author author = new Author("12345", "Lev", "Tolstoy");
        Genre genre = new Genre("2134", "novel");
        Book bookToInsert = new Book("Anna Karenina", author, genre);
        Book savedBook = bookRepository.save(bookToInsert);
        assertThat(bookToInsert.getId()).isNotNull();

        assertThat(savedBook).matches(book -> book.getTitle().equals("Anna Karenina"))
                .matches(book -> book.getAuthor().equals(author))
                .matches(book -> book.getGenre().equals(genre));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateTitleOfExistingBook() {
        String bookId = "45632";
        Book bookFromDB = mongoTemplate.findById(bookId, Book.class);

        Book infoToUpdate = new Book(bookId, "Anna Karenina", bookFromDB.getAuthor(), bookFromDB.getGenre());
        Book result = bookRepository.save(infoToUpdate);
        assertThat(result)
                .usingRecursiveComparison().isEqualTo(infoToUpdate)
                .isNotEqualTo(bookFromDB);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingBook() {
        String bookId = "45633";
        Book bookFromDB = mongoTemplate.findById(bookId, Book.class);
        assertThat(bookFromDB).isNotNull();
        assertThat(bookRepository.count()).isEqualTo(3);

        bookRepository.deleteById(bookId);
        assertThat(bookRepository.count()).isEqualTo(2);
        assertThat(mongoTemplate.findById(bookId, Book.class)).isNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteCommentsForDeletedBook() {
        String bookId = "45634";
        Aggregation aggregation = newAggregation(match(Criteria.where("book_id").is(bookId)));
        Optional<Book> bookFromDB = bookRepository.findById(bookId);
        assertThat(bookFromDB).isPresent();

        List<Comment> comments = mongoTemplate.aggregate(aggregation, Comment.class, Comment.class).getMappedResults();
        assertThat(comments.size()).isEqualTo(2);

        bookRepository.deleteById(bookId);
        assertThat(mongoTemplate.aggregate(aggregation, Comment.class, Comment.class).getMappedResults()).isEmpty();
    }

    @Test
    void shouldReturnAllBooksCount() {
        assertThat(bookRepository.count()).isEqualTo(3L);
    }

}