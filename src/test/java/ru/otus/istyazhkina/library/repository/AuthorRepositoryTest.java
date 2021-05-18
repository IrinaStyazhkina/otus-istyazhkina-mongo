package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.events.MongoAuthorOperationsEventListener;
import ru.otus.istyazhkina.library.exceptions.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalSaveOperationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataMongoTest
@Import(MongoAuthorOperationsEventListener.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnAuthorForExistingId() {
        Optional<Author> actualAuthor = authorRepository.findById("12345");
        assertThat(actualAuthor).isPresent().get()
                .matches((author) -> author.getName().equals("Lev"))
                .matches((author) -> author.getSurname().equals("Tolstoy"));
    }

    @Test
    void shouldReturnEmptyOptionalIfAuthorByIdNotExists() {
        Optional<Author> author = authorRepository.findById("12134");
        assertThat(author).isEmpty();
    }

    @Test
    void shouldReturnAuthorForExistingName() {
        Optional<Author> authorByName = authorRepository.findByNameAndSurname("Lev", "Tolstoy");
        assertThat(authorByName).get().isEqualTo(new Author("12345", "Lev", "Tolstoy"));
    }

    @Test
    void shouldThrowReturnEmptyOptionalIfAuthorByNameNotExists() {
        Optional<Author> author = authorRepository.findByNameAndSurname("Ivan", "Ivanov");
        assertThat(author).isEmpty();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewAuthor() {
        Author author = new Author("Petr", "Petrov");
        Author savedAuthor = authorRepository.save(author);

        assertThat(author.getId()).isNotNull();

        assertThat(savedAuthor)
                .matches(a -> a.getName().equals("Petr"))
                .matches(a -> a.getSurname().equals("Petrov"));
    }

    @Test
    void shouldNotInsertAuthorWithSameNameAndSurname() {
        Author authorToInsert = new Author("Lev", "Tolstoy");
        assertThatThrownBy(() -> authorRepository.save(authorToInsert))
                .isInstanceOf(IllegalSaveOperationException.class)
                .hasMessage("Same author already exists");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingAuthor() {
        String authorId = "12346";
        Author authorFromDB = authorRepository.findById(authorId).get();

        Author infoToUpdate = new Author(authorId, "Aleksander", "Pushkin");
        Author result = authorRepository.save(infoToUpdate);

        assertThat(result)
                .usingRecursiveComparison().isEqualTo(infoToUpdate)
                .isNotEqualTo(authorFromDB);
    }

    @Test
    void shouldThrowExceptionWhileUpdateIfAuthorWithNewNameExists() {
        Author authorToUpdate = new Author("12347", "Lev", "Tolstoy");
        assertThatThrownBy(() -> authorRepository.save(authorToUpdate))
                .isInstanceOf(IllegalSaveOperationException.class)
                .hasMessage("Same author already exists");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingAuthorIfNoBookIsBound() {
        Optional<Author> authorFromDB = authorRepository.findById("12348");
        assertThat(authorFromDB).isPresent();
        assertThat(authorRepository.count()).isEqualTo(4);

        authorRepository.deleteById("12348");
        assertThat(authorRepository.count()).isEqualTo(3);
        assertThat(authorRepository.findById("12348")).isEmpty();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> authorRepository.deleteById("12345")).isInstanceOf(IllegalDeleteOperationException.class)
                .hasMessage("Can not delete author because exists book with this author");
    }
}