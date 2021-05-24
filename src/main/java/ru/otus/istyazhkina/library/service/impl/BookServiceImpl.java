package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public long getBooksCount() {
        return bookRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(String id) throws DataOperationException {
        return bookRepository.findById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByTitle(String name) {
        return bookRepository.findByTitle(name);
    }

    @Override
    @Transactional
    public Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName) {
        Author author = authorRepository
                .findByNameAndSurname(authorName, authorSurname)
                .orElseGet(() -> authorRepository.save(new Author(authorName, authorSurname)));

        Genre genre = genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(new Genre(genreName)));

        Book book = new Book(bookTitle, author, genre);
        bookRepository.save(book);
        return book;
    }

    @Override
    @Transactional
    public Book updateBookTitle(String id, String newTitle) throws DataOperationException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found"));
        book.setTitle(newTitle);
        bookRepository.save(book);
        return book;
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public void deleteBookById(String id) throws DataOperationException {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataOperationException("There is no book with provided id");
        }
    }
}
