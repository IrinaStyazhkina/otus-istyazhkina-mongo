package ru.otus.istyazhkina.library.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.domain.entity.Book;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.utils.HystrixSleepUtil;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public long getBooksCount() {
        return bookRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "books", fallbackMethod = "fallbackGetAllBooks")
    public List<Book> getAllBooks() {
        HystrixSleepUtil.sleepRandomly(5);
        return bookRepository.findAll();
    }

    public List<Book> fallbackGetAllBooks() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "books", fallbackMethod = "fallbackGetBookById")
    public Book getBookById(String id) throws DataOperationException {
        HystrixSleepUtil.sleepRandomly(5);
        return bookRepository.findById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found"));
    }

    public Book fallbackGetBookById(String id) {
        return Book.builder()
                .id(id)
                .title("N/A")
                .author(new Author("N/A", "N/A"))
                .genre(new Genre("N/A"))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "books", fallbackMethod = "fallbackGetBooksByTitle")
    public List<Book> getBooksByTitle(String name) {
        HystrixSleepUtil.sleepRandomly(5);
        return bookRepository.findByTitle(name);
    }

    public List<Book> fallbackGetBooksByTitle(String name) {
        Book book = Book.builder()
                .id("N/A")
                .title(name)
                .author(new Author("N/A", "N/A"))
                .genre(new Genre("N/A"))
                .build();
        return List.of(book);
    }

    @Override
    @Transactional
    public Book addNewBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    @Override
    @Transactional
    public Book updateBook(String id, Book book) throws DataOperationException {
        Book bookFromDB = bookRepository.findById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found"));
        bookFromDB.setTitle(book.getTitle());
        bookFromDB.setGenre(book.getGenre());
        bookFromDB.setAuthor(book.getAuthor());
        bookRepository.save(bookFromDB);
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
