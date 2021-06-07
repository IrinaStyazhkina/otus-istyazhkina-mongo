package ru.otus.istyazhkina.library.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.istyazhkina.library.domain.jpa.Author;
import ru.otus.istyazhkina.library.domain.jpa.Book;
import ru.otus.istyazhkina.library.domain.jpa.Comment;
import ru.otus.istyazhkina.library.domain.jpa.Genre;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.CommentRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private final List<Author> authors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "irinastyazhkina", runAlways = true)
    public void dropDB(MongoDatabase mongoDatabase) {
        mongoDatabase.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "irinastyazhkina", runAlways = true)
    public void initAuthors(AuthorRepository authorRepository) {
        Author Tolstoy = authorRepository.save(new Author("Lev", "Tolstoy"));
        Author Brodskiy = authorRepository.save(new Author("Joseph", "Brodskiy"));
        Author Tolkien = authorRepository.save(new Author("John", "Tolkien"));
        authors.addAll(List.of(Tolstoy, Brodskiy, Tolkien));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "irinastyazhkina", runAlways = true)
    public void initGenres(GenreRepository genreRepository) {
        Genre novel = genreRepository.save(new Genre("novel"));
        Genre poetry = genreRepository.save(new Genre("poetry"));
        Genre fantasy = genreRepository.save(new Genre("fantasy"));
        Genre fiction = genreRepository.save(new Genre("fiction"));
        genres.addAll(List.of(novel, poetry, fantasy, fiction));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "irinastyazhkina", runAlways = true)
    public void initBooks(BookRepository bookRepository) {
        Book warAndPeace = bookRepository.save(new Book("War and Peace", authors.get(0), genres.get(0)));
        Book rozhdestvenskieStikhi = bookRepository.save(new Book("Rozhdestvenskie stikhi", authors.get(1), genres.get(1)));
        Book theHobbit = bookRepository.save(new Book("The Hobbit", authors.get(2), genres.get(2)));
        books.addAll(List.of(warAndPeace, rozhdestvenskieStikhi, theHobbit));
    }

    @ChangeSet(order = "004", id = "initComments", author = "irinastyazhkina", runAlways = true)
    public void initComments(CommentRepository commentRepository) {
        commentRepository.save(new Comment("The 10 Greatest Books of All Time", books.get(0)));
        commentRepository.save(new Comment("Story about hobbit Bilbo Baggins", books.get(2)));
        commentRepository.save(new Comment("Nominated for the Carnegie Medal and awarded a prize from the New York Herald Tribune for best juvenile fiction", books.get(2)));
    }


}
