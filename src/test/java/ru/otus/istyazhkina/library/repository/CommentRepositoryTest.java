package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    Book testBook = new Book("45632", "War and Peace", new Author("12345", "Lev", "Tolstoy"), new Genre("2134", "novel"));

    @Test
    void shouldReturnCommentForExistingId() {
        Optional<Comment> actualComment = commentRepository.findById("9087");
        assertThat(actualComment).isPresent().get()
                .matches(comment -> comment.getContent().equals("The 10 Greatest Books of All Time"))
                .matches(comment -> comment.getBook().equals(testBook));
    }

    @Test
    void shouldReturnEmptyOptionalIfCommentByIdNotExists() {
        Optional<Comment> comment = commentRepository.findById("10");
        assertThat(comment).isEmpty();
    }

    @Test
    void shouldReturnCommentListForGetAllComments() {
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewComment() {
        Comment newComment = new Comment("Golden collection", testBook);

        Comment savedComment = commentRepository.save(newComment);
        assertThat(newComment.getId()).isNotNull();

        assertThat(savedComment)
                .matches(comment -> comment.getContent().equals("Golden collection"))
                .matches(comment -> comment.getBook().equals(testBook));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingComment() {
        String commentId = "9087";
        Comment commentFromDB = findCommentById(commentId);
        Comment infoToUpdate = new Comment(commentId, "The best of Russian classics", commentFromDB.getBook());
        Comment result = commentRepository.save(infoToUpdate);

        assertThat(result).usingRecursiveComparison()
                .isEqualTo(infoToUpdate)
                .isNotEqualTo(commentFromDB);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingComment() {
        String commentId = "9089";
        Comment commentFromDB = findCommentById(commentId);
        assertThat(commentFromDB).isNotNull();
        assertThat(commentRepository.count()).isEqualTo(3);

        commentRepository.deleteById(commentId);
        assertThat(commentRepository.count()).isEqualTo(2);
        assertThat(findCommentById(commentId)).isNull();
    }


    @Test
    void shouldReturnCommentsByBookId() {
        List<Comment> commentsByBookId = commentRepository.findAllByBookId("45634");
        assertThat(commentsByBookId).containsExactlyInAnyOrder(
                commentRepository.findById("9088").get(),
                commentRepository.findById("9089").get());
    }

    private Comment findCommentById(String id) {
        Aggregation aggregation = newAggregation(match(Criteria.where("id").is(id)));
        List<Comment> mappedResults = mongoTemplate.aggregate(aggregation, Comment.class, Comment.class).getMappedResults();
        return mappedResults.size() == 1 ? mappedResults.get(0) : null;
    }
}
