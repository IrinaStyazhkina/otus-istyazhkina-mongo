package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;
import ru.otus.istyazhkina.library.service.BookService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceImplIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldAddAuthorAndGenreAndBookWhileInsertIfTheyNotExist() {
        assertThat(genreRepository.findByName("play")).isEmpty();
        assertThat(authorRepository.findByNameAndSurname("Anton", "Chekhov")).isEmpty();

        Book book = bookService.addNewBook("Seagull", "Anton", "Chekhov", "play");
        assertThat(genreRepository.findByName("play")).isNotNull();
        assertThat(authorRepository.findByNameAndSurname("Anton", "Chekhov")).isNotNull();
        assertThat(book.getTitle()).isEqualTo("Seagull");
        assertThat(book.getGenre().getName()).isEqualTo("play");
        assertThat(book.getAuthor().getName()).isEqualTo("Anton");
        assertThat(book.getAuthor().getSurname()).isEqualTo("Chekhov");
    }
}
