package ru.otus.istyazhkina.library.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.IllegalDeleteOperationException;
import ru.otus.istyazhkina.library.exceptions.IllegalSaveOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;

import java.util.List;
import java.util.Optional;

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
        List<Book> allByAuthorId = bookRepository.findAllByAuthorId(authorId);

        if (allByAuthorId.size() > 0)
            throw new IllegalDeleteOperationException("Can not delete author because exists book with this author");
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Author> event) {
        super.onBeforeSave(event);
        Author author = event.getSource();
        Optional<Author> dataFromDB = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());

        if (dataFromDB.isPresent()) throw new IllegalSaveOperationException("Same author already exists");
    }
}
