package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.jpa.Book;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;
import ru.otus.istyazhkina.library.domain.rest.BookDTO;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@AutoConfigureDataMongo
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private BookDTO bookDTO = new BookDTO("1", "Anna Karenina", new AuthorDTO("1", "Lev", "Tolstoy"), new GenreDTO("1", "novel"));
    private Book book = BookDTO.toBook(bookDTO);


    @Test
    void shouldReturnBooksList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(book));
        mockMvc.perform(get("/books"))
                .andExpect(status().is(200))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", List.of(bookDTO)));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        when(bookService.getBookById("1")).thenReturn(book);
        mockMvc.perform(get("/books/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", bookDTO))
                .andExpect(model().attribute("add", false));
    }

    @Test
    void shouldReturnAddBookPage() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().is(200))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", new BookDTO()))
                .andExpect(model().attribute("add", true));
    }

    @Test
    void shouldRedirectAfterAddBookToAllBooks() throws Exception {
        when(bookService.addNewBook(book)).thenReturn(book);
        mockMvc.perform(post("/books/add")
                .contentType("application/x-www-form-urlencoded")
                .content("title=War+and+Peace&authorDTO=1%2CLev%2CTolstoy&genreDTO=1%2Cnovel"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    void shouldRedirectAfterUpdateBookToAllBooks() throws Exception {
        when(bookService.updateBook("1", book)).thenReturn(book);
        mockMvc.perform(post("/books/1")
                .contentType("application/x-www-form-urlencoded")
                .content("title=War+and+Peace&authorDTO=1%2CLev%2CTolstoy&genreDTO=1%2Cnovel"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/books"));
    }

    @Test
    void redirectToAllBooksAfterDeleteBook() throws Exception {
        mockMvc.perform(post("/books/delete/1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/books"));

    }
}