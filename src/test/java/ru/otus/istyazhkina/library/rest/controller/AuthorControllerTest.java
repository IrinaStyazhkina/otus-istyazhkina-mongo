package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.rest.AppExceptionHandler;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import({AuthorController.class, AppExceptionHandler.class})
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorService authorService;

    private final Author author = new Author("1", "Lev", "Tolstoy");
    private final String arrayJsonContent = "[{\"id\":\"1\",\"name\":\"Lev\", \"surname\":\"Tolstoy\"}]";
    private final String authorJson = "{\"id\":\"1\",\"name\":\"Lev\", \"surname\":\"Tolstoy\"}";

    @Test
    void shouldReturnAuthorsList() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of(author));
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().is(200))
                .andExpect(content().json(arrayJsonContent));
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        when(authorService.getAuthorById("1")).thenReturn(author);
        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().is(200))
                .andExpect(content().json(authorJson));
    }

    @Test
    void shouldCreateNewAuthor() throws Exception {
        Author newAuthor = new Author("4", "Ivan", "Turgenev");
        String newAuthorJson = "{\"id\":\"4\",\"name\":\"Ivan\", \"surname\":\"Turgenev\"}";
        when(authorService.addNewAuthor(new Author("Ivan", "Turgenev"))).thenReturn(newAuthor);
        mockMvc.perform(post("/authors/add")
                .contentType("application/json;charset=utf-8")
                .content("{\"name\":\"Ivan\", \"surname\":\"Turgenev\"}"))
                .andExpect(status().is(201))
                .andExpect(content().json(newAuthorJson));
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        when(authorService.updateAuthor("1", author)).thenReturn(author);
        mockMvc.perform(put("/authors/1")
                .contentType("application/json;charset=utf-8")
                .content(authorJson))
                .andExpect(status().is(200))
                .andExpect(content().json(authorJson));
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/authors/3"))
                .andExpect(status().is(200));
    }

    @Test
    void shouldReturnExceptionWhileAddExistingAuthor() throws Exception {
        when(authorService.addNewAuthor(author))
                .thenThrow(DataOperationException.class);

        mockMvc.perform(post("/authors/add")
                .contentType("application/json;charset=utf-8")
                .content(authorJson))
                .andExpect(status().is(400));
    }

    @Test
    void shouldReturnExceptionWhileUpdate() throws Exception {
        when(authorService.updateAuthor("2", author))
                .thenThrow(DataOperationException.class);

        mockMvc.perform(put("/authors/2")
                .contentType("application/json;charset=utf-8")
                .content(authorJson))
                .andExpect(status().is(400));
    }
}