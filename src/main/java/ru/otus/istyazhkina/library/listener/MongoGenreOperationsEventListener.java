package ru.otus.istyazhkina.library.listener;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.exception.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exception.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;

@Component
@RequiredArgsConstructor
public class MongoGenreOperationsEventListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        Document source = event.getSource();
        String genreId = source.get("_id").toString();

        if (bookRepository.existsByGenreId(genreId))
            throw new IllegalDeleteOperationException("Can not delete genre because exists book with this genre");
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Genre> event) {
        super.onBeforeSave(event);
        Genre genre = event.getSource();
        if (genreRepository.existsByName(genre.getName())) {
            throw new IllegalSaveOperationException("Same genre already exists");
        }
    }
}
