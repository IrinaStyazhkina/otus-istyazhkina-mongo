package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.domain.jpa.Genre;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
@AutoConfigureDataMongo
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    private GenreDTO genreDTO = new GenreDTO("1", "novel");
    private Genre genre = GenreDTO.toGenre(genreDTO);


    @Test
    void shouldReturnGenresList() throws Exception {
        when(genreService.getAllGenres()).thenReturn(List.of(genre));
        mockMvc.perform(get("/genres"))
                .andExpect(status().is(200))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", List.of(genreDTO)));
    }

    @Test
    void shouldReturnGenreById() throws Exception {
        when(genreService.getGenreById("1")).thenReturn(genre);
        mockMvc.perform(get("/genres/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("genre"))
                .andExpect(model().attribute("genre", genreDTO))
                .andExpect(model().attribute("add", false));
    }

    @Test
    void shouldReturnAddGenrePage() throws Exception {
        mockMvc.perform(get("/genres/add"))
                .andExpect(status().is(200))
                .andExpect(view().name("genre"))
                .andExpect(model().attribute("genre", new GenreDTO()))
                .andExpect(model().attribute("add", true));
    }

    @Test
    void shouldRedirectAfterAddGenreToAllGenres() throws Exception {
        when(genreService.addNewGenre(genre)).thenReturn(genre);
        mockMvc.perform(post("/genres/add")
                .contentType("application/x-www-form-urlencoded")
                .content("name=poetry"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/genres"));
    }

    @Test
    void shouldRedirectAfterUpdateGenreToAllGenres() throws Exception {
        when(genreService.updateGenre("1", genre)).thenReturn(genre);
        mockMvc.perform(post("/genres/update/1")
                .contentType("application/x-www-form-urlencoded")
                .content("name=novel"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/genres"));
    }

    @Test
    void redirectToALlGenresAfterDeleteGenre() throws Exception {
        mockMvc.perform(post("/genres/delete/1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/genres"));

    }
}