package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    Comment getCommentById(String id) throws DataOperationException;

    Comment addNewComment(String name, String bookId) throws DataOperationException;

    Comment updateCommentContent(String id, String newContent) throws DataOperationException;

    void deleteComment(String id) throws DataOperationException;

    List<Comment> getCommentsByBookId(String bookId);
}
