package ru.otus.istyazhkina.library.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

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

        List<Book> allByGenreId = bookRepository.findAllByGenreId(genreId);
        if (allByGenreId.size() > 0)
            throw new IllegalDeleteOperationException("Can not delete genre because exists book with this genre");
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Genre> event) {
        super.onBeforeSave(event);
        Genre genre = event.getSource();
        Optional<Genre> dataFromDB = genreRepository.findByName(genre.getName());

        if (dataFromDB.isPresent()) throw new IllegalSaveOperationException("Same genre already exists");

    }
}
