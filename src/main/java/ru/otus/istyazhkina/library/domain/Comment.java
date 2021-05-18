package ru.otus.istyazhkina.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "comment")
public class Comment {

    @Id
    private String id;

    @Field(name = "content")
    private String content;

    private Book book;

    public Comment(String id, String content, Book book) {
        this.id = id;
        this.content = content;
        this.book = book;
    }

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(book, comment.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, book);
    }

    @Override
    public String toString() {
        return String.format("%s\t|\t%s\t|\t%s", id, content, book.getTitle());
    }
}
