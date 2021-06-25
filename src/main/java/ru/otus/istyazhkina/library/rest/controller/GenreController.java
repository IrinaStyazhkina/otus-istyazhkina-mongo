package ru.otus.istyazhkina.library.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.GenreService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDTO> getAllGenres() {
        return genreService.getAllGenres().stream().map(GenreDTO::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/genres/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO getGenre(@PathVariable("genreId") String genreId) throws DataOperationException {
        return GenreDTO.toDto(genreService.getGenreById(genreId));
    }


    @PutMapping("/genres/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO updateGenre(@PathVariable("genreId") String genreId, @RequestBody GenreDTO genreDTO) throws DataOperationException {
        Genre genre = GenreDTO.toGenre(genreDTO);
        return GenreDTO.toDto(genreService.updateGenre(genreId, genre));
    }

    @PostMapping("/genres/add")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDTO addGenre(@Valid @RequestBody GenreDTO genreDTO) throws DataOperationException {
        Genre genre = GenreDTO.toGenre(genreDTO);
        return GenreDTO.toDto(genreService.addNewGenre(genre));
    }

    @DeleteMapping("/genres/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGenre(@PathVariable("genreId") String genreId) throws DataOperationException {
        genreService.deleteGenre(genreId);
    }
}
