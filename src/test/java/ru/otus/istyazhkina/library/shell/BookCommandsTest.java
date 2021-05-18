package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BookCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private BookService bookService;

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all books");
        assertThat(res).isEqualTo("No data in table 'Books'");
    }

    @Test
    void shouldReturnBooks() {
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of
                (new Book("d5r6ft7g8y9uh", "Anna Karenina", new Author("345678", "Lev", "Tolstoy"), new Genre("34567huhi", "novel")),
                        new Book("65r7t68gyiu", "Harry Potter", new Author("5r76tygi", "Joanne", "Rowling"), new Genre("5r7ftguy", "fantasy"))));
        Object res = shell.evaluate(() -> "all books");
        assertThat(res).isEqualTo("d5r6ft7g8y9uh\t|\tAnna Karenina\t|\tLev Tolstoy\t|\tnovel\n65r7t68gyiu\t|\tHarry Potter\t|\tJoanne Rowling\t|\tfantasy\n");
    }

    @Test
    void checkMessageWhileGettingBookByNotExistingId() throws DataOperationException {
        Mockito.when(bookService.getBookById("1")).thenThrow(new DataOperationException("Book by provided ID not found in database"));
        Object res = shell.evaluate(() -> "book by id 1");
        assertThat(res).isEqualTo("Book by provided ID not found in database");
    }

    @Test
    void shouldReturnBookNameById() throws DataOperationException {
        Mockito.when(bookService.getBookById("ftyguhbjn6")).thenReturn(new Book("ftyguhbjn6", "Harry Potter", new Author("d67f8gy", "Joanne", "Rowling"), new Genre("6e57r68t7", "fantasy")));
        Object res = shell.evaluate(() -> "book by id ftyguhbjn6");
        assertThat(res).isEqualTo("Harry Potter");
    }

    @Test
    void checkMessageWhileGettingBookByNotExistingTitle() {
        Mockito.when(bookService.getBooksByTitle("not_found")).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "book by title not_found");
        assertThat(res).isEqualTo("No books found by provided title");
    }

    @Test
    void shouldReturnBookIdByTitle() {
        Mockito.when(bookService.getBooksByTitle("1984")).thenReturn(List.of(new Book("4e56r576t8gyiuh", "1984", new Author("r65tyfugjh", "George", "Orwell"), new Genre("6tyutyrt", "dystopia"))));
        Object res = shell.evaluate(() -> "book by title 1984");
        assertThat(res).isEqualTo("Id's of books with provided title:\n4e56r576t8gyiuh");
    }

    @Test
    void checkMessageOnDeleteBook() throws DataOperationException {
        Mockito.doNothing().when(bookService).deleteBookById("7647239847");
        Object res = shell.evaluate(() -> "delete book 7647239847");
        assertThat(res).isEqualTo("Book with this ID no longer exists");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() throws DataOperationException {
        Mockito.doThrow(new DataOperationException("There is no book with provided id")).when(bookService).deleteBookById("30");
        Object res = shell.evaluate(() -> "delete book 30");
        assertThat(res).isEqualTo("There is no book with provided id");
    }

    @Test
    void checkMessageWhileAddingNewBook() {
        Book book = new Book("4565r76t87y", "1984", new Author("5r76t87y", "George", "Orwell"), new Genre("45768t7y", "dystopia"));
        Mockito.when(bookService.addNewBook("1984", "George", "Orwell", "dystopia")).thenReturn(book);
        Object res = shell.evaluate(() -> "add book 1984 George Orwell dystopia");
        assertThat(res).isEqualTo("Book with title 1984 successfully added!");
    }


    @Test
    void checkMessageOnUpDateBook() throws DataOperationException {
        Book book = new Book("5r7tu", "1984", new Author("5r6t879y", "George", "Orwell"), new Genre("r768t79y", "dystopia"));
        Mockito.when(bookService.updateBookTitle("5r7tu", "1984")).thenReturn(book);
        Object res = shell.evaluate(() -> "update book title 5r7tu 1984");
        assertThat(res).isEqualTo("Book with id 5r7tu is successfully updated. Book's title is 1984 ");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() throws DataOperationException {
        DataOperationException e = new DataOperationException("Book by provided ID not found in database");
        Mockito.when(bookService.updateBookTitle("65r768t7", "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "update book title 65r768t7 exception");
        assertThat(res).isEqualTo(e.getMessage());
    }
}