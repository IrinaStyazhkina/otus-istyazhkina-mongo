package ru.otus.istyazhkina.library.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PagesController {

    @GetMapping("/genres")
    public String getAllGenresPage() {
        return "genres";
    }

    @GetMapping("/genres/{genreId}")
    public String getExistingGenrePage(@PathVariable("genreId") String genreId) {
        return "genre";
    }

    @GetMapping("/genres/add")
    public String getNewGenrePage() {
        return "genre";
    }

    @GetMapping("/authors")
    public String getAllAuthorsPage() {
        return "authors";
    }

    @GetMapping("/authors/{authorId}")
    public String getExistingAuthorPage(@PathVariable("authorId") String authorId) {
        return "author";
    }

    @GetMapping("/authors/add")
    public String getNewAuthorPage() {
        return "author";
    }

    @GetMapping("/books")
    public String getAllBooksPage() {
        return "books";
    }

    @GetMapping("/books/{bookId}")
    public String getExistingBookPage(@PathVariable("bookId") String bookId) {
        return "book";
    }

    @GetMapping("/books/add")
    public String getNewBookPage() {
        return "book";
    }


}
