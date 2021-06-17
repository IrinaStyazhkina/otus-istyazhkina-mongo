package ru.otus.istyazhkina.library.domain.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.istyazhkina.library.domain.jpa.Book;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookDTO {

    String id;
    String title;
    AuthorDTO authorDTO;
    GenreDTO genreDTO;

    public static Book toBook(BookDTO dto) {
        return Book.builder().title(dto.getTitle()).build();
    }

    public static BookDTO toDto(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), AuthorDTO.toDto(book.getAuthor()), GenreDTO.toDto(book.getGenre()));
    }
}
