package ru.otus.istyazhkina.library.rest.converter;

import org.springframework.core.convert.converter.Converter;
import ru.otus.istyazhkina.library.domain.rest.AuthorDTO;

public class StringToAuthorDTOConverter implements Converter<String, AuthorDTO> {

    @Override
    public AuthorDTO convert(String from) {
        String[] data = from.split(",");
        return new AuthorDTO(data[0], data[1], data[2]);
    }
}
