package ru.otus.istyazhkina.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.istyazhkina.library.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByBookId(String id);

    List<Comment> findAll();

    void deleteAllByBookId(String bookId);
}
