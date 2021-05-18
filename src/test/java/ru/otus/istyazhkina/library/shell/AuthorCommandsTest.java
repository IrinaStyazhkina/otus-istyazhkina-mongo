package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private AuthorService authorService;

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(authorService.getAllAuthors()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all authors");
        assertThat(res).isEqualTo("No data in table 'Authors'");
    }

    @Test
    void shouldReturnAuthors() {
        Mockito.when(authorService.getAllAuthors()).thenReturn(List.of(new Author("12345", "Ivan", "Ivanov"), new Author("456789", "Petr", "Petrov")));
        Object res = shell.evaluate(() -> "all authors");
        assertThat(res).isEqualTo("12345\t|\tIvan\t|\tIvanov\n456789\t|\tPetr\t|\tPetrov\n");
    }

    @Test
    void checkMessageWhileGettingAuthorByNotExistingId() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author by provided ID not found in database");
        Mockito.when(authorService.getAuthorById("1")).thenThrow(e);
        Object res = shell.evaluate(() -> "author by id 1");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnAuthorById() throws DataOperationException {
        Mockito.when(authorService.getAuthorById("2")).thenReturn(new Author("15627615", "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by id 2");
        assertThat(res).isEqualTo("15627615\t|\tAlexander\t|\tPushkin");
    }

    @Test
    void checkMessageWhileGettingAuthorByNotExistingName() throws DataOperationException {
        DataOperationException e = new DataOperationException("No Author found by name Vasya Veselov");
        Mockito.when(authorService.getAuthorByName("Vasya", "Veselov")).thenThrow(e);
        Object res = shell.evaluate(() -> "author by name Vasya Veselov");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnAuthorByName() throws DataOperationException {
        Mockito.when(authorService.getAuthorByName("Alexander", "Pushkin")).thenReturn(new Author("2345678", "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by name Alexander Pushkin");
        assertThat(res).isEqualTo("2345678\t|\tAlexander\t|\tPushkin");
    }

    @Test
    void checkMessageOnDeleteAuthor() throws DataOperationException {
        Mockito.doNothing().when(authorService).deleteAuthor("3");
        Object res = shell.evaluate(() -> "delete author 3");
        assertThat(res).isEqualTo("The author with this ID no longer exists");
    }

    @Test
    void checkMessageOnExceptionWhileDelete() throws DataOperationException {
        Mockito.doThrow(new DataOperationException("There is no author with provided id")).when(authorService).deleteAuthor("30");
        Object res = shell.evaluate(() -> "delete author 30");
        assertThat(res).isEqualTo("There is no author with provided id");
    }

    @Test
    void checkMessageOnExceptionWhileAdd() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author with this name and surname already exists in database");
        Mockito.when(authorService.addNewAuthor("duplicate", "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "add author duplicate exception");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateAuthorName() throws DataOperationException {
        Author author = new Author("3456789", "Aleksey", "Tolstoy");
        Mockito.when(authorService.updateAuthor("3456789", "Aleksey", "Tolstoy")).thenReturn(author);
        Object res = shell.evaluate(() -> "update author 3456789 Aleksey Tolstoy");
        assertThat(res).isEqualTo("Author with id 3456789 successfully updated. Author's name is Aleksey Tolstoy");
    }

    @Test
    void checkMessageOnExceptionWhileUpdateName() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author by provided ID not found in database");
        Mockito.when(authorService.updateAuthor("3", "Ivan", "Ivanov")).thenThrow(e);
        Object res = shell.evaluate(() -> "update author 3 Ivan Ivanov");
        assertThat(res).isEqualTo(e.getMessage());
    }
}