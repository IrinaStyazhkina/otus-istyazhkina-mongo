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
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors()
                .stream()
                .map(AuthorDTO::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/authors/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO getAuthorById(@PathVariable("authorId") String authorId) throws DataOperationException {
        return AuthorDTO.toDto(authorService.getAuthorById(authorId));
    }

    @PutMapping("/authors/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO updateAuthor(@PathVariable("authorId") String auhtorId, @RequestBody AuthorDTO authorDTO) throws DataOperationException {
        Author author = AuthorDTO.toAuthor(authorDTO);
        return AuthorDTO.toDto(authorService.updateAuthor(auhtorId, author));
    }

    @PostMapping("/authors/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@RequestBody AuthorDTO authorDTO) throws DataOperationException {
        Author author = AuthorDTO.toAuthor(authorDTO);
        return AuthorDTO.toDto(authorService.addNewAuthor(author));
    }

    @DeleteMapping("/authors/{authorId}")
    public void deleteAuthor(@PathVariable("authorId") String authorId) throws DataOperationException {
        authorService.deleteAuthor(authorId);
    }
}
