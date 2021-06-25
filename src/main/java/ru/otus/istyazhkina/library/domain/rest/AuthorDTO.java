package ru.otus.istyazhkina.library.domain.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.istyazhkina.library.domain.entity.Author;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthorDTO {

    String id;
    String name;
    String surname;

    @Override
    public String toString() {
        return id + "," + name + "," + surname;
    }

    public static Author toAuthor(AuthorDTO dto) {
        return new Author(dto.getId(), dto.getName(), dto.getSurname());
    }

    public static AuthorDTO toDto(Author author) {
        return new AuthorDTO(author.getId(), author.getName(), author.getSurname());
    }

}
