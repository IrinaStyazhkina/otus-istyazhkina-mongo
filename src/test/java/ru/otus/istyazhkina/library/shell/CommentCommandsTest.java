package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private CommentService commentService;

    private final Book existingBook = new Book("2354e657687", "Anna Karenina", new Author("34567", "Lev", "Tolstoy"), new Genre("e5465r76tyu", "novel"));

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(commentService.getAllComments()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all comments");
        assertThat(res).isEqualTo("No data in table 'Comments'");
    }

    @Test
    void shouldReturnComments() {
        Mockito.when(commentService.getAllComments()).thenReturn(List.of(new Comment("65r76t87y", "comment1", existingBook), new Comment("56t879y", "comment2", existingBook)));
        Object res = shell.evaluate(() -> "all comments");
        assertThat(res).isEqualTo("65r76t87y\t|\tcomment1\t|\tAnna Karenina\n56t879y\t|\tcomment2\t|\tAnna Karenina\n");
    }

    @Test
    void checkMessageWhileGettingCommentByNotExistingId() throws DataOperationException {
        DataOperationException e = new DataOperationException("Comment by provided ID not found in database");
        Mockito.when(commentService.getCommentById("5r7tuyg")).thenThrow(e);
        Object res = shell.evaluate(() -> "comment by id 5r7tuyg");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnCommentById() throws DataOperationException {
        Mockito.when(commentService.getCommentById("4e56rtuyghiu")).thenReturn(new Comment("4e56rtuyghiu", "test comment", existingBook));
        Object res = shell.evaluate(() -> "comment by id 4e56rtuyghiu");
        assertThat(res).isEqualTo("4e56rtuyghiu\t|\ttest comment\t|\tAnna Karenina");
    }

    @Test
    void checkMessageOnDeleteComment() throws DataOperationException {
        Mockito.doNothing().when(commentService).deleteComment("cfygvuhbkjnlk");
        Object res = shell.evaluate(() -> "delete comment cfygvuhbkjnlk");
        assertThat(res).isEqualTo("Comment with this ID no longer exists");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() throws DataOperationException {
        Mockito.doThrow(new DataOperationException("Deletion is not successful. Please check if provided comment id exists")).when(commentService).deleteComment("30");
        Object res = shell.evaluate(() -> "delete comment 30");
        assertThat(res).isEqualTo("Deletion is not successful. Please check if provided comment id exists");
    }

    @Test
    void checkMessageWhileAddingNewComment() throws DataOperationException {
        Mockito.when(commentService.addNewComment("test", "345678")).thenReturn(new Comment("test", existingBook));
        Object res = shell.evaluate(() -> "add comment test 345678");
        assertThat(res).isEqualTo("Comment successfully added!");
    }

    @Test
    void checkMessageWhileAddingNewCommentForNotExistingBook() throws DataOperationException {
        DataOperationException e = new DataOperationException("Can not add new Comment. Book by provided id is not found!\"");
        Mockito.when(commentService.addNewComment("test", "2345678")).thenThrow(e);
        Object res = shell.evaluate(() -> "add comment test 2345678");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpdateComment() throws DataOperationException {
        Comment comment = new Comment("wert765432", "test_comment", existingBook);
        Mockito.when(commentService.updateCommentContent("wert765432", "test_comment")).thenReturn(comment);
        Object res = shell.evaluate(() -> "update comment wert765432 test_comment");
        assertThat(res).isEqualTo("Comment with id wert765432 successfully updated. Comment is test_comment");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() throws DataOperationException {
        DataOperationException e = new DataOperationException("Can not update comment. Comment by provided ID not found in database.");
        Mockito.when(commentService.updateCommentContent("34567890ghjk", "test")).thenThrow(e);
        Object res = shell.evaluate(() -> "update comment 34567890ghjk test");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnCommentsByBookId() {
        Mockito.when(commentService.getCommentsByBookId("23456789")).thenReturn(List.of(new Comment("146732", "comment1", existingBook), new Comment("5678654", "comment2", existingBook)));
        Object res = shell.evaluate(() -> "comments by book id 23456789");
        assertThat(res).isEqualTo("Comments:\n" +
                "146732\t|\tcomment1\t|\tAnna Karenina\n" +
                "5678654\t|\tcomment2\t|\tAnna Karenina\n");
    }
}