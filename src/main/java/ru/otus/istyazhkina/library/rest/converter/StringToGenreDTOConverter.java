package ru.otus.istyazhkina.library.rest.converter;

import org.springframework.core.convert.converter.Converter;
import ru.otus.istyazhkina.library.domain.rest.GenreDTO;

public class StringToGenreDTOConverter implements Converter<String, GenreDTO> {

    @Override
    public GenreDTO convert(String from) {
        String[] data = from.split(",");
        return new GenreDTO(data[0], data[1]);
    }
}
