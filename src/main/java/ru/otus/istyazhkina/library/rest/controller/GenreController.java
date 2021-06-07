package ru.otus.istyazhkina.library.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.istyazhkina.library.domain.jpa.Genre;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String getAllGenres(Model model) {
        List<GenreDTO> genres = genreService.getAllGenres()
                .stream()
                .map(GenreDTO::toDto)
                .collect(Collectors.toList());
        model.addAttribute("genres", genres);
        return "genres";
    }

    @GetMapping("/genres/{genreId}")
    public String getGenreById(@PathVariable("genreId") String genreId, Model model) throws DataOperationException {
        Genre genre = genreService.getGenreById(genreId);
        model.addAttribute("genre", GenreDTO.toDto(genre));
        model.addAttribute("add", false);
        return "genre";
    }

    @GetMapping("genres/add")
    public String getAddGenrePage(Model model) {
        GenreDTO genre = new GenreDTO();
        model.addAttribute("genre", genre);
        model.addAttribute("add", true);
        return "genre";
    }

    @PostMapping("genres/update/{genreId}")
    public String updateGenre(@PathVariable("genreId") String genreId, GenreDTO genreDTO) throws DataOperationException {
        Genre genre = GenreDTO.toGenre(genreDTO);
        genreService.updateGenre(genreId, genre);
        return "redirect:/genres";
    }

    @PostMapping("genres/add")
    public String addGenre(GenreDTO genreDTO, BindingResult bindingResult, ModelMap model) throws DataOperationException {
        if (bindingResult.hasErrors()) {
            return "redirect:/genres";
        }
        Genre genre = GenreDTO.toGenre(genreDTO);
        genreService.addNewGenre(genre);
        model.clear();
        return "redirect:/genres";
    }

    @PostMapping("genres/delete/{genreId}")
    public String deleteGenre(@PathVariable("genreId") String genreId) throws DataOperationException {
        genreService.deleteGenre(genreId);
        return "redirect:/genres";
    }

}
