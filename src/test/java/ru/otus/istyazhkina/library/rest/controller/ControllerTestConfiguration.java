package ru.otus.istyazhkina.library.rest.controller;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import static org.mockito.Mockito.mock;


@SpringBootConfiguration
public class ControllerTestConfiguration {

    @Bean
    public GenreService genreService() {
        return mock(GenreService.class);
    }

    @Bean
    public AuthorService authorService() {
        return mock(AuthorService.class);
    }

    @Bean
    public BookService bookService() {
        return mock(BookService.class);
    }

}
