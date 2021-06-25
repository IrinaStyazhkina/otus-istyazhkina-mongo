package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.exception.DataOperationException;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(String id) throws DataOperationException;

    Author getAuthorByName(String name, String surname) throws DataOperationException;

    Author addNewAuthor(Author author) throws DataOperationException;

    Author updateAuthor(String id, Author auhtor) throws DataOperationException;

    void deleteAuthor(String id) throws DataOperationException;
}
