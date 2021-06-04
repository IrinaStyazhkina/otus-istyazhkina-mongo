package ru.otus.istyazhkina.library.listener;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.exception.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exception.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoAuthorOperationsEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        Document source = event.getSource();
        String authorId = source.get("_id").toString();

        if (bookRepository.existsByAuthorId(authorId))
            throw new IllegalDeleteOperationException("Can not delete author because exists book with this author");
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Author> event) {
        super.onBeforeSave(event);
        Author author = event.getSource();

        if (authorRepository.existsByNameAndSurname(author.getName(), author.getSurname())) {
            throw new IllegalSaveOperationException("Same author already exists");
        }
    }
}
