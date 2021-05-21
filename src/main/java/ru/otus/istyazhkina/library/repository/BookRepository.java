package ru.otus.istyazhkina.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.istyazhkina.library.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByTitle(String title);

    List<Book> findAllByAuthorId(String authorId);

    List<Book> findAllByGenreId(String genreId);

    boolean existsByAuthorId(String id);

    boolean existsByGenreId(String id);
}
