package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.exception.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exception.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.GenreRepository;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(String id) throws DataOperationException {
        return genreRepository.findById(id).orElseThrow(() -> new DataOperationException("No genre found by provided id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByName(String name) throws DataOperationException {
        return genreRepository.findByName(name).orElseThrow(() -> new DataOperationException("No genre found by provided name"));
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre addNewGenre(Genre genre) throws DataOperationException {
        try {
            return genreRepository.save(genre);
        } catch (IllegalSaveOperationException e) {
            throw new DataOperationException("Can not add genre because genre already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre updateGenre(String id, Genre genre) throws DataOperationException {
        Genre genreFromDB = genreRepository.findById(id).orElseThrow(() -> new DataOperationException("Can not update genre. Genre by provided ID not found"));
        if (genre.getName().equals(genreFromDB.getName())) {
            return genreFromDB;
        }
        genreFromDB.setName(genre.getName());
        try {
            return genreRepository.save(genreFromDB);
        } catch (IllegalSaveOperationException e) {
            throw new DataOperationException("Can not update genre because genre with same name already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public void deleteGenre(String id) throws DataOperationException {
        try {
            genreRepository.deleteById(id);
        } catch (IllegalDeleteOperationException e) {
            throw new DataOperationException("You can not delete genre because exists book with this genre!");
        }
    }
}
