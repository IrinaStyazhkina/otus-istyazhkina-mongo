package ru.otus.istyazhkina.library.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.istyazhkina.library.domain.jpa.Book;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;
import ru.otus.istyazhkina.library.domain.rest.BookDTO;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @ModelAttribute("allAuthors")
    public List<AuthorDTO> allAuthors() {
        return authorService.getAllAuthors()
                .stream()
                .map(AuthorDTO::toDto)
                .collect(Collectors.toList());
    }

    @ModelAttribute("allGenres")
    public List<GenreDTO> allGenres() {
        return genreService.getAllGenres()
                .stream()
                .map(GenreDTO::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<BookDTO> books = bookService.getAllBooks()
                .stream()
                .map(BookDTO::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{bookId}")
    public String getBookById(@PathVariable("bookId") String bookId, Model model) throws DataOperationException {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", BookDTO.toDto(book));
        model.addAttribute("add", false);
        return "book";
    }

    @GetMapping("books/add")
    public String getAddAuthorPage(Model model) {
        BookDTO bookDTO = new BookDTO();
        model.addAttribute("book", bookDTO);
        model.addAttribute("add", true);
        return "book";
    }

    @PostMapping("/books/{bookId}")
    public String updateBook(@PathVariable("bookId") String bookId, BookDTO bookDTO) throws DataOperationException {
        Book book = BookDTO.toBook(bookDTO);
        bookService.updateBook(bookId, book);
        return "redirect:/books";
    }

    @PostMapping("books/add")
    public String addBook(BookDTO bookDTO, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/books";
        }
        Book book = BookDTO.toBook(bookDTO);
        bookService.addNewBook(book);
        model.clear();
        return "redirect:/books";
    }

    @PostMapping("books/delete/{bookId}")
    public String deleteBook(@PathVariable("bookId") String bookId) throws DataOperationException {
        bookService.deleteBookById(bookId);
        return "redirect:/books";
    }

}
