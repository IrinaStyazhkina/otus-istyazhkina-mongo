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
import ru.otus.istyazhkina.library.domain.jpa.Book;
import ru.otus.istyazhkina.library.domain.rest.BookDTO;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/api/books")
    @ResponseStatus(HttpStatus.OK)
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks()
                .stream()
                .map(BookDTO::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO getBookById(@PathVariable("bookId") String bookId) throws DataOperationException {
        return BookDTO.toDto(bookService.getBookById(bookId));
    }

    @PutMapping("/books/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO updateBook(@PathVariable("bookId") String bookId, @RequestBody BookDTO bookDTO) throws DataOperationException {
        Book book = BookDTO.toBook(bookDTO);
        book.setAuthor(authorService.getAuthorById(bookDTO.getAuthorDTO().getId()));
        book.setGenre(genreService.getGenreById(bookDTO.getGenreDTO().getId()));
        return BookDTO.toDto(bookService.updateBook(bookId, book));
    }

    @PostMapping("/books/add")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO addBook(@RequestBody BookDTO bookDTO) throws DataOperationException {
        Book book = BookDTO.toBook(bookDTO);
        book.setAuthor(authorService.getAuthorById(bookDTO.getAuthorDTO().getId()));
        book.setGenre(genreService.getGenreById(bookDTO.getGenreDTO().getId()));
        return BookDTO.toDto(bookService.addNewBook(book));
    }

    @DeleteMapping("/books/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("bookId") String bookId) throws DataOperationException {
        bookService.deleteBookById(bookId);
    }

}
