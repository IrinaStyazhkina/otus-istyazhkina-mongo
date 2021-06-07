package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.jpa.Book;
import ru.otus.istyazhkina.library.exception.DataOperationException;

import java.util.List;

public interface BookService {

    long getBooksCount();

    List<Book> getAllBooks();

    Book getBookById(String id) throws DataOperationException;

    List<Book> getBooksByTitle(String name);

    Book addNewBook(Book book);

    Book updateBook(String id, Book book) throws DataOperationException;

    void deleteBookById(String id) throws DataOperationException;
}
