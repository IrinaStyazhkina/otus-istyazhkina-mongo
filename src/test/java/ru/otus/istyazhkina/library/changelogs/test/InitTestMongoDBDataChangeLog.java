package ru.otus.istyazhkina.library.changelogs.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.istyazhkina.library.domain.entity.Author;
import ru.otus.istyazhkina.library.domain.entity.Book;
import ru.otus.istyazhkina.library.domain.entity.Comment;
import ru.otus.istyazhkina.library.domain.entity.Genre;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.repository.CommentRepository;
import ru.otus.istyazhkina.library.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitTestMongoDBDataChangeLog {

    private final List<Author> authors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "irinastyazhkina", runAlways = true)
    public void dropDB(MongoDatabase mongoDatabase) {
        mongoDatabase.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "irinastyazhkina", runAlways = true)
    public void initTestDB(AuthorRepository authorRepository) {
        Author Tolstoy = authorRepository.save(new Author("12345", "Lev", "Tolstoy"));
        Author Brodskiy = authorRepository.save(new Author("12346", "Joseph", "Brodskiy"));
        Author Tolkien = authorRepository.save(new Author("12347", "John", "Tolkien"));
        Author Kuprin = authorRepository.save(new Author("12348", "Aleksandr", "Kuprin"));

        authors.addAll(List.of(Tolstoy, Brodskiy, Tolkien, Kuprin));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "irinastyazhkina", runAlways = true)
    public void initGenres(GenreRepository genreRepository) {
        Genre novel = genreRepository.save(new Genre("2134", "novel"));
        Genre poetry = genreRepository.save(new Genre("2135", "poetry"));
        Genre fantasy = genreRepository.save(new Genre("2136", "fantasy"));
        Genre fairytale = genreRepository.save(new Genre("2137", "fairytale"));
        genres.addAll(List.of(novel, poetry, fantasy, fairytale));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "irinastyazhkina", runAlways = true)
    public void initBooks(BookRepository bookRepository) {
        Book warAndPeace = bookRepository.save(new Book("45632", "War and Peace", authors.get(0), genres.get(0)));
        Book rozhdestvenskieStikhi = bookRepository.save(new Book("45633", "Rozhdestvenskie stikhi", authors.get(1), genres.get(1)));
        Book theHobbit = bookRepository.save(new Book("45634", "The Hobbit", authors.get(2), genres.get(2)));
        books.addAll(List.of(warAndPeace, rozhdestvenskieStikhi, theHobbit));
    }

    @ChangeSet(order = "004", id = "initComments", author = "irinastyazhkina", runAlways = true)
    public void initComments(CommentRepository commentRepository) {
        commentRepository.save(new Comment("9087", "The 10 Greatest Books of All Time", books.get(0)));
        commentRepository.save(new Comment("9088", "Story about hobbit Bilbo Baggins", books.get(2)));
        commentRepository.save(new Comment("9089", "Nominated for the Carnegie Medal and awarded a prize from the New York Herald Tribune for best juvenile fiction", books.get(2)));
    }


}
