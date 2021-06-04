package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@AutoConfigureDataMongo
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    private AuthorDTO authorDTO = new AuthorDTO("1", "Lev", "Tolstoy");
    private Author author = AuthorDTO.toAuthor(authorDTO);


    @Test
    void shouldReturnAuthorsList() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of(author));
        mockMvc.perform(get("/authors"))
                .andExpect(status().is(200))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", List.of(authorDTO)));
    }

    @Test
    void shouldReturnAuthorById() throws Exception {
        when(authorService.getAuthorById("1")).thenReturn(author);
        mockMvc.perform(get("/authors/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("author"))
                .andExpect(model().attribute("author", authorDTO))
                .andExpect(model().attribute("add", false));
    }

    @Test
    void shouldReturnAddAuthorPage() throws Exception {
        mockMvc.perform(get("/authors/add"))
                .andExpect(status().is(200))
                .andExpect(view().name("author"))
                .andExpect(model().attribute("author", new AuthorDTO()))
                .andExpect(model().attribute("add", true));
    }

    @Test
    void shouldRedirectAfterAddAuthorToAllAuthors() throws Exception {
        when(authorService.addNewAuthor(author)).thenReturn(author);
        mockMvc.perform(post("/authors/add")
                .contentType("application/x-www-form-urlencoded")
                .content("name=Lev&surname=Tolstoy"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/authors"));
    }

    @Test
    void shouldRedirectAfterUpdateAuthorToAllAuthors() throws Exception {
        when(authorService.updateAuthor("1", author)).thenReturn(author);
        mockMvc.perform(post("/authors/update/1")
                .contentType("application/x-www-form-urlencoded")
                .content("name=Lev&surname=Tolstoy"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/authors"));
    }

    @Test
    void redirectToAllAuthorsAfterDeleteAuthor() throws Exception {
        mockMvc.perform(post("/authors/delete/1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/authors"));

    }

}