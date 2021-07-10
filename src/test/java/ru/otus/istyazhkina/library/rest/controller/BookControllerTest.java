package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.domain.entity.Book;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.rest.AppExceptionHandler;
import ru.otus.istyazhkina.library.security.SecurityConfiguration;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@Import({BookController.class, AppExceptionHandler.class})
@ContextConfiguration(classes = {SecurityConfiguration.class, ControllerTestConfiguration.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    private static final Book book = new Book("1", "Anna Karenina", new Author("1", "Lev", "Tolstoy"), new Genre("1", "novel"));
    private static final String arrayJsonContent = "[{\"id\":\"1\",\"title\":\"Anna Karenina\",\"authorDTO\":{\"id\":\"1\",\"name\":\"Lev\",\"surname\":\"Tolstoy\"},\"genreDTO\":{\"id\":\"1\",\"name\":\"novel\"}}]";
    private static final String bookJson = "{\"id\":\"1\",\"title\":\"Anna Karenina\",\"authorDTO\":{\"id\":\"1\",\"name\":\"Lev\",\"surname\":\"Tolstoy\"},\"genreDTO\":{\"id\":\"1\",\"name\":\"novel\"}}";

    @Test
    @WithMockUser
    void shouldReturnBooksList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(book));
        mockMvc.perform(get("/api/books"))
                .andExpect(status().is(200))
                .andExpect(content().json(arrayJsonContent));
    }

    @Test
    @WithMockUser
    void shouldReturnBookById() throws Exception {
        when(bookService.getBookById("1")).thenReturn(book);
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().is(200))
                .andExpect(content().json(bookJson));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewBookForAdminUser() throws Exception {
        Book newBook = new Book("4", "War and Peace", new Author("1", "Lev", "Tolstoy"), new Genre("1", "novel"));
        String newBookJson = "{\"id\":\"4\",\"title\":\"War and Peace\",\"authorDTO\":{\"id\":\"1\",\"name\":\"Lev\",\"surname\":\"Tolstoy\"},\"genreDTO\":{\"id\":\"1\",\"name\":\"novel\"}}";
        when(authorService.getAuthorById("1")).thenReturn(new Author("1", "Lev", "Tolstoy"));
        when(genreService.getGenreById("1")).thenReturn(new Genre("1", "novel"));
        when(bookService.addNewBook(new Book("War and Peace", new Author("1", "Lev", "Tolstoy"), new Genre("1", "novel")))).thenReturn(newBook);
        mockMvc.perform(post("/books/add")
                .contentType("application/json;charset=utf-8")
                .content("{\"title\":\"War and Peace\",\"authorDTO\":{\"id\":\"1\",\"name\":\"Lev\",\"surname\":\"Tolstoy\"},\"genreDTO\":{\"id\":\"1\",\"name\":\"novel\"}}"))
                .andExpect(status().is(201))
                .andExpect(content().json(newBookJson));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotCreateNewBookForSimpleUser() throws Exception {
        mockMvc.perform(post("/books/add")
                .contentType("application/json;charset=utf-8")
                .content("{\"title\":\"War and Peace\",\"authorDTO\":{\"id\":\"1\",\"name\":\"Lev\",\"surname\":\"Tolstoy\"},\"genreDTO\":{\"id\":\"1\",\"name\":\"novel\"}}"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateBookForAdminUser() throws Exception {
        when(bookService.updateBook("1", new Book("Anna Karenina", new Author("1", "Lev", "Tolstoy"), new Genre("1", "novel")))).thenReturn(book);
        when(authorService.getAuthorById("1")).thenReturn(new Author("1", "Lev", "Tolstoy"));
        when(genreService.getGenreById("1")).thenReturn(new Genre("1", "novel"));
        mockMvc.perform(put("/books/1")
                .contentType("application/json;charset=utf-8")
                .content(bookJson))
                .andExpect(status().is(200))
                .andExpect(content().json(bookJson));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotUpdateBookForSimpleUser() throws Exception {
        mockMvc.perform(put("/books/1")
                .contentType("application/json;charset=utf-8")
                .content(bookJson))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteBookForAdminUser() throws Exception {
        mockMvc.perform(delete("/books/3"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotDeleteBookForSimpleUser() throws Exception {
        mockMvc.perform(delete("/books/3"))
                .andExpect(status().is(403));
    }

}