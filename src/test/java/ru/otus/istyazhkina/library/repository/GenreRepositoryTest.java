package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.events.MongoGenreOperationsEventListener;
import ru.otus.istyazhkina.library.exceptions.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalSaveOperationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import(MongoGenreOperationsEventListener.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnGenreForExistingId() {
        Optional<Genre> actualGenre = genreRepository.findById("2134");
        assertThat(actualGenre).isPresent().get()
                .matches(genre -> genre.getName().equals("novel"));
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByIdNotExists() {
        Optional<Genre> genre = genreRepository.findById("10");
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenreForExistingName() {
        Optional<Genre> genreByName = genreRepository.findByName("novel");
        assertThat(genreByName).get().isEqualTo(new Genre("2134", "novel"));
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByNameNotExists() {
        Optional<Genre> genre = genreRepository.findByName("novel123");
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenresListForGetAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewGenre() {
        Genre genreToInsert = new Genre("criminal");
        Genre savedGenre = genreRepository.save(genreToInsert);

        assertThat(genreToInsert.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("criminal");
    }

    @Test
    void shouldNotInsertGenreWithSameName() {
        Genre genreToInsert = new Genre("novel");
        assertThatThrownBy(() -> genreRepository.save(genreToInsert))
                .isInstanceOf(IllegalSaveOperationException.class)
                .hasMessage("Same genre already exists");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingGenre() {
        String genreId = "2136";
        Genre genreFromDB = genreRepository.findById(genreId).get();

        Genre infoToUpdate = new Genre(genreId, "science fiction");
        Genre result = genreRepository.save(infoToUpdate);

        assertThat(result).usingRecursiveComparison()
                .isEqualTo(infoToUpdate)
                .isNotEqualTo(genreFromDB);
    }

    @Test
    void shouldThrowExceptionWhileUpdateIfGenreWithNewNameExists() {
        Genre genreToUpdate = new Genre("2135", "novel");
        assertThatThrownBy(() -> genreRepository.save(genreToUpdate))
                .isInstanceOf(IllegalSaveOperationException.class)
                .hasMessage("Same genre already exists");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingGenreIfNoBookIsBound() {
        Optional<Genre> genreFromDB = genreRepository.findById("2137");
        assertThat(genreFromDB).isPresent();
        assertThat(genreRepository.count()).isEqualTo(4);

        genreRepository.deleteById("2137");
        assertThat(genreRepository.count()).isEqualTo(3);
        assertThat(genreRepository.findById("2137")).isEmpty();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> genreRepository.deleteById("2134")).isInstanceOf(IllegalDeleteOperationException.class)
                .hasMessage("Can not delete genre because exists book with this genre");
    }
}