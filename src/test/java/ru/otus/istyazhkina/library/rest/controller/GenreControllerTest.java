package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.rest.AppExceptionHandler;
import ru.otus.istyazhkina.library.security.SecurityConfiguration;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@Import({GenreController.class, AppExceptionHandler.class})
@ContextConfiguration(classes = {SecurityConfiguration.class, ControllerTestConfiguration.class})
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreService genreService;

    private static final Genre genre = new Genre("1", "novel");
    private static final String arrayJsonContent = "[{\"id\":\"1\",\"name\":\"novel\"}]";
    private static final String genreJson = "{\"id\":\"1\",\"name\":\"novel\"}";


    @Test
    @WithMockUser
    void shouldReturnGenresList() throws Exception {
        when(genreService.getAllGenres()).thenReturn(List.of(genre));
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().is(200))
                .andExpect(content().json(arrayJsonContent));
    }

    @Test
    @WithMockUser
    void shouldReturnGenreById() throws Exception {
        when(genreService.getGenreById("1")).thenReturn(genre);
        mockMvc.perform(get("/api/genres/1"))
                .andExpect(status().is(200))
                .andExpect(content().json(genreJson));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewGenreForAdminUser() throws Exception {
        Genre newGenre = new Genre("4", "fantasy");
        String newGenreJson = "{\"id\":\"4\",\"name\":\"fantasy\"}";
        when(genreService.addNewGenre(new Genre("fantasy"))).thenReturn(newGenre);
        mockMvc.perform(post("/genres/add")
                .contentType("application/json;charset=utf-8")
                .content("{\"name\":\"fantasy\"}"))
                .andExpect(status().is(201))
                .andExpect(content().json(newGenreJson));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotAllowCreateNewGenreForSimpleUser() throws Exception {
        mockMvc.perform(post("/genres/add")
                .contentType("application/json;charset=utf-8")
                .content("{\"name\":\"fantasy\"}"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateGenreForAdminUser() throws Exception {
        when(genreService.updateGenre("1", genre)).thenReturn(genre);
        mockMvc.perform(put("/genres/1")
                .contentType("application/json;charset=utf-8")
                .content(genreJson))
                .andExpect(status().is(200))
                .andExpect(content().json(genreJson));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotUpdateGenreForSimpleUser() throws Exception {
        mockMvc.perform(put("/genres/1")
                .contentType("application/json;charset=utf-8")
                .content(genreJson))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteGenreForAdminUser() throws Exception {
        mockMvc.perform(delete("/genres/3"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldNotDeleteGenreForSimpleUser() throws Exception {
        mockMvc.perform(delete("/genres/3"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnExceptionWhileAddExistingGenre() throws Exception {
        when(genreService.addNewGenre(genre))
                .thenThrow(DataOperationException.class);

        mockMvc.perform(post("/genres/add")
                .contentType("application/json;charset=utf-8")
                .content(genreJson))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnExceptionWhileUpdate() throws Exception {
        when(genreService.updateGenre("2", genre))
                .thenThrow(DataOperationException.class);

        mockMvc.perform(put("/genres/2")
                .contentType("application/json;charset=utf-8")
                .content(genreJson))
                .andExpect(status().is(400));
    }
}