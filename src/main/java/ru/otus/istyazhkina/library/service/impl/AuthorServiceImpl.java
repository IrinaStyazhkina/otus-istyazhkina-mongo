package ru.otus.istyazhkina.library.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.exception.DataOperationException;
import ru.otus.istyazhkina.library.exception.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exception.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.utils.HystrixSleepUtil;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "authors", fallbackMethod = "fallbackGetAllAuthors")
    public List<Author> getAllAuthors() {
        HystrixSleepUtil.sleepRandomly(4);
        return authorRepository.findAll();
    }

    public List<Author> fallbackGetAllAuthors() {
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "authors", fallbackMethod = "fallbackGetAuthorById")
    public Author getAuthorById(String id) throws DataOperationException {
        HystrixSleepUtil.sleepRandomly(4);
        return authorRepository.findById(id).orElseThrow(() -> new DataOperationException("Author by provided ID not found"));
    }

    public Author fallbackGetAuthorById(String id) {
        return Author.builder()
                .id(id)
                .name("N/A")
                .surname("N/A")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @HystrixCommand(commandKey = "authors", fallbackMethod = "fallbackGetAuthorByName")
    public Author getAuthorByName(String name, String surname) throws DataOperationException {
        HystrixSleepUtil.sleepRandomly(4);
        return authorRepository.findByNameAndSurname(name, surname).orElseThrow(() -> new DataOperationException("No author found by provided name"));
    }

    public Author fallbackGetAuthorByName(String name, String surname) {
        return Author.builder()
                .id("N/A")
                .name(name)
                .surname(surname)
                .build();
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author addNewAuthor(Author author) throws DataOperationException {
        try {
            return authorRepository.save(author);
        } catch (IllegalSaveOperationException e) {
            throw new DataOperationException("Can not add author because author already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author updateAuthor(String id, Author author) throws DataOperationException {
        Author authorFromDB = authorRepository.findById(id).orElseThrow(() -> new DataOperationException("Can not update author. Author by provided ID not found"));
        if (author.getName().equals(authorFromDB.getName()) && author.getSurname().equals(authorFromDB.getSurname())) {
            return authorFromDB;
        }
        authorFromDB.setName(author.getName());
        authorFromDB.setSurname(author.getSurname());
        try {
            return authorRepository.save(authorFromDB);
        } catch (IllegalSaveOperationException e) {
            throw new DataOperationException("Can not update author because author with same name already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public void deleteAuthor(String id) throws DataOperationException {
        try {
            authorRepository.deleteById(id);
        } catch (IllegalDeleteOperationException e) {
            throw new DataOperationException("You can not delete author because exists book with this author!");
        }

    }
}
