package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(String id) throws DataOperationException;

    Genre getGenreByName(String name) throws DataOperationException;

    Genre addNewGenre(String name) throws DataOperationException;

    Genre updateGenresName(String id, String newName) throws DataOperationException;

    void deleteGenre(String id) throws DataOperationException;
}
