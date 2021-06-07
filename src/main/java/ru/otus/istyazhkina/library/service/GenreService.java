package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.jpa.Genre;
import ru.otus.istyazhkina.library.exception.DataOperationException;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(String id) throws DataOperationException;

    Genre getGenreByName(String name) throws DataOperationException;

    Genre addNewGenre(Genre genre) throws DataOperationException;

    Genre updateGenre(String id, Genre genre) throws DataOperationException;

    void deleteGenre(String id) throws DataOperationException;
}
