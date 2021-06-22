package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.domain.entity.Book;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(BookServiceImpl.class)
class BookServiceImplIntegrationTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddAuthorAndGenreAndBookWhileInsertIfTheyNotExist() {
        assertThat(genreRepository.findByName("play")).isEmpty();
        assertThat(authorRepository.findByNameAndSurname("Anton", "Chekhov")).isEmpty();

        Book book = bookService.addNewBook(new Book("Seagull", new Author("Anton", "Chekhov"), new Genre("play")));
        assertThat(genreRepository.findByName("play")).isNotNull();
        assertThat(authorRepository.findByNameAndSurname("Anton", "Chekhov")).isNotNull();
        assertThat(book.getTitle()).isEqualTo("Seagull");
        assertThat(book.getGenre().getName()).isEqualTo("play");
        assertThat(book.getAuthor().getName()).isEqualTo("Anton");
        assertThat(book.getAuthor().getSurname()).isEqualTo("Chekhov");
    }
}
