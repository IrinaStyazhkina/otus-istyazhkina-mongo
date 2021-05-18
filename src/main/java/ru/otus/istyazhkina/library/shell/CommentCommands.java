package ru.otus.istyazhkina.library.shell;


import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Get all comments from table", key = {"all comments"})
    public String getAllComments() {
        List<Comment> allComments = commentService.getAllComments();
        if (allComments.size() == 0) {
            return "No data in table 'Comments'";
        }
        StringBuilder sb = new StringBuilder();
        for (Comment comment : allComments) {
            sb.append(comment).append("\n");
        }
        return sb.toString();
    }

    @ShellMethod(value = "Get comment by ID", key = {"comment by id"})
    public String getCommentById(@ShellOption String id) {
        try {
            Comment comment = commentService.getCommentById(id);
            return comment.toString();
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Update name of a comment by its ID", key = {"update comment"})
    public String updateCommentContent(@ShellOption String id, @ShellOption String newContent) {
        try {
            Comment comment = commentService.updateCommentContent(id, newContent);
            return String.format("Comment with id %s successfully updated. Comment is %s", comment.getId(), comment.getContent());
        } catch (DataOperationException e) {
            return e.getMessage();
        }

    }

    @ShellMethod(value = "Add new comment", key = {"add comment"})
    public String addNewComment(@ShellOption String content, @ShellOption String bookId) {
        try {
            commentService.addNewComment(content, bookId);
            return "Comment successfully added!";
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete comment by ID", key = {"delete comment"})
    public String deleteCommentById(@ShellOption String id) {
        try {
            commentService.deleteComment(id);
            return "Comment with this ID no longer exists";
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Get all comments for book by book id", key = {"comments by book id"})
    public String getCommentsByBookId(@ShellOption String bookId) {
        List<Comment> allComments = commentService.getCommentsByBookId(bookId);
        StringBuilder sb = new StringBuilder();
        sb.append("Comments:\n");
        for (Comment comment : allComments) {
            sb.append(comment).append("\n");
        }
        return sb.toString();
    }
}
