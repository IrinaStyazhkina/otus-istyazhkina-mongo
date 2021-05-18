package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.CommentRepository;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getCommentById(String id) throws DataOperationException {
        return commentRepository.findById(id).orElseThrow(() -> new DataOperationException("Comment by provided ID not found"));
    }

    @Override
    @Transactional
    public Comment addNewComment(String content, String bookId) throws DataOperationException {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new DataOperationException("Can not add new Comment. Book by provided id is not found!"));
        Comment comment = new Comment(content, book);
        return commentRepository.save(new Comment(content, book));
    }

    @Override
    @Transactional
    public Comment updateCommentContent(String id, String newContent) throws DataOperationException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new DataOperationException("Can not update comment. Comment by provided ID not found"));
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public void deleteComment(String id) throws DataOperationException {
        try {
            commentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataOperationException("There is no comment with provided id");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId);
    }
}
