package ru.otus.istyazhkina.library.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        List<AuthorDTO> authors = authorService.getAllAuthors()
                .stream()
                .map(AuthorDTO::toDto)
                .collect(Collectors.toList());
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/authors/{authorId}")
    public String getAuthorById(@PathVariable("authorId") String authorId, Model model) throws DataOperationException {
        Author author = authorService.getAuthorById(authorId);
        model.addAttribute("author", AuthorDTO.toDto(author));
        model.addAttribute("add", false);
        return "author";
    }

    @GetMapping("authors/add")
    public String getAddAuthorPage(Model model) {
        AuthorDTO authorDTO = new AuthorDTO();
        model.addAttribute("author", authorDTO);
        model.addAttribute("add", true);
        return "author";
    }

    @PostMapping("authors/update/{authorId}")
    public String updateAuthor(@PathVariable("authorId") String auhtorId, AuthorDTO authorDTO) throws DataOperationException {
        Author author = AuthorDTO.toAuthor(authorDTO);
        authorService.updateAuthor(auhtorId, author);
        return "redirect:/authors";
    }

    @PostMapping("authors/add")
    public String addAuthor(AuthorDTO authorDTO, BindingResult bindingResult, ModelMap model) throws DataOperationException {
        if (bindingResult.hasErrors()) {
            return "redirect:/authors";
        }
        Author author = AuthorDTO.toAuthor(authorDTO);
        authorService.addNewAuthor(author);
        model.clear();
        return "redirect:/authors";
    }

    @PostMapping("authors/delete/{authorId}")
    public String deleteAuthor(@PathVariable("authorId") String authorId) throws DataOperationException {
        authorService.deleteAuthor(authorId);
        return "redirect:/authors";
    }

}
