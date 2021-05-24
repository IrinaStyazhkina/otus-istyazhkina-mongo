package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorById(String id) throws DataOperationException {
        return authorRepository.findById(id).orElseThrow(() -> new DataOperationException("Author by provided ID not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorByName(String name, String surname) throws DataOperationException {
        return authorRepository.findByNameAndSurname(name, surname).orElseThrow(() -> new DataOperationException("No author found by provided name"));
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author addNewAuthor(String name, String surname) throws DataOperationException {
        try {
            Author author = new Author(name, surname);
            return authorRepository.save(author);
        } catch (IllegalSaveOperationException e) {
            throw new DataOperationException("Can not add author because author already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author updateAuthor(String id, String newName, String newSurname) throws DataOperationException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new DataOperationException("Can not update author. Author by provided ID not found"));
        author.setName(newName);
        author.setSurname(newSurname);
        try {
            return authorRepository.save(author);
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
