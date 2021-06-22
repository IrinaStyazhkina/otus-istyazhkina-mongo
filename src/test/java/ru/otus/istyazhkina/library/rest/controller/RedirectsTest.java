package ru.otus.istyazhkina.library.rest.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.istyazhkina.library.security.SecurityConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({AuthorController.class, GenreController.class, BookController.class})
@ContextConfiguration(classes = {SecurityConfiguration.class, ControllerTestConfiguration.class})
public class RedirectsTest {

    @Autowired
    MockMvc mockMvc;

    @CsvSource({"/api/authors", "/api/genres", "/api/books"})
    @ParameterizedTest
    void shouldRedirectUnauthorisedUserToLoginPage(String checkedUrl) throws Exception {
        mockMvc.perform(get(checkedUrl))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
