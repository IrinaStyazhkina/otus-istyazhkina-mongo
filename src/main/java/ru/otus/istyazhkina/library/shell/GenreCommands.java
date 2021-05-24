package ru.otus.istyazhkina.library.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    @ShellMethod(value = "Get all genres from table", key = {"all genres"})
    public String getAllGenres() {
        List<Genre> allGenres = genreService.getAllGenres();
        if (allGenres.size() == 0) {
            return "No data in table 'Genres'";
        }
        StringBuilder sb = new StringBuilder();
        for (Genre genre : allGenres) {
            sb.append(genre).append("\n");
        }
        return sb.toString();
    }

    @ShellMethod(value = "Get genre by ID", key = {"genre by id"})
    public String getGenreById(@ShellOption String id) {
        try {
            Genre genre = genreService.getGenreById(id);
            return genre.toString();
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Update name of a genre by its ID", key = {"update genre"})
    public String updateGenreName(@ShellOption String id, @ShellOption String newName) {
        try {
            Genre genre = genreService.updateGenresName(id, newName);
            return String.format("Genre with id %s successfully updated. Genre name is %s", genre.getId(), genre.getName());
        } catch (DataOperationException e) {
            return e.getMessage();
        }

    }

    @ShellMethod(value = "Add new genre", key = {"add genre"})
    public String addNewGenre(@ShellOption String name) {
        try {
            genreService.addNewGenre(name);
            return String.format("Genre with name %s successfully added!", name);
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Get genre's ID by its name", key = {"genre by name"})
    public String getGenreByName(@ShellOption String name) {
        try {
            Genre genreByName = genreService.getGenreByName(name);
            return genreByName.toString();
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete genre by ID", key = {"delete genre"})
    public String deleteGenreById(@ShellOption String id) {
        try {
            genreService.deleteGenre(id);
            return "Genre with this ID no longer exists";
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }
}
