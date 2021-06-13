package ru.otus.istyazhkina.library.domain.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.istyazhkina.library.domain.jpa.Genre;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GenreDTO {
    private String id;
    private String name;

    public static Genre toGenre(GenreDTO dto) {
        return new Genre(dto.getId(), dto.getName());
    }

    public static GenreDTO toDto(Genre genre) {
        return new GenreDTO(genre.getId(), genre.getName());
    }

    @Override
    public String toString() {
        return name;
    }

}
