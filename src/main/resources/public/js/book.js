function loadAllBooksData() {
    $.get("/api/books").done(function (books) {
        books.forEach(function (book) {
            $("tbody").append(`
              <tr class="table_row">
                    <td class="table_cell">${book.id}</td>
                    <td class="table_cell">${book.title}</td>
                    <td class="table_cell"">${book.authorDTO.name} ${book.authorDTO.surname}</td>
                    <td class="table_cell">${book.genreDTO.name}</td>
                    <td class="table_cell">
                        <a class="table_edit-link" href="/books/${book.id}">Edit</a>
                    </td>
                    <td class="table_cell">
                            <button type="submit" onclick="deleteBook('${book.id}')" class="delete_button">Delete</button>
                    </td>
                </tr>
            `);
        })
    });
}

function loadBookData() {
    const path = window.location.pathname;
    const pathLastPart = path.substr(path.lastIndexOf("/") + 1);
    if (pathLastPart === "add") {
        $(".form_wrapper").append(`
            <form action="/books/add" class="item_form" id="add-book" method="post" onsubmit="addBook(); return false;">
                <label class="item_label" for="book_title">Title</label>
                <input class="item_input" id="book_title" value="" required>
                <label class="item_label" for="book_author">Author</label>
                <select class="item_select" id="book_author">
                </select>
                <label class="item_label" for="book_genre">Genre</label>
                <select class="item_select" id="book_genre">
                </select>
                <button class="item_btn" type="submit" id="save">Save</button>
            </form>
        `);
        getAuthors();
        getGenres();
    } else {

        $.get("/api/books/" + pathLastPart).done(function (book) {
            $(".form_wrapper").append(`
                <form class="item_form" id="edit-book" action="/books/${pathLastPart}" method="put" name="book" onsubmit="updateBook(); return false;">
                        <label class="item_label" for="book_id">ID</label>
                        <input class="item_input book_input--disabled" id="book_id"  value="${book.id}" disabled>
                    <label class="item_label" for="book_title">Title</label>
                        <input class="item_input" id="book_title" value="${book.title}" required>
                    <label class="item_label" for="book_author">Author</label>
                        <select class="item_select" id="book_author" name="author">
                        </select>
                        <label class="item_label" for="book_genre">Genre</label>
                        <select class="item_select" id="book_genre" name="genre">
                        </select>
                        <button class="item_btn" type="submit">Save</button>
                </form> 

            `);
            getAuthorsAndSelect(book.authorDTO.id);
            getGenresAndSelect(book.genreDTO.id)
        });
    }
}

function getAuthors() {
    $.get("/api/authors").done(function (authors) {
        authors.forEach(function (author) {
            $("#book_author").append(`
                    <option value=${author.id}>
                        ${author.name} ${author.surname}
                    </option>
                `);
        })
    });
}

function getAuthorsAndSelect(author) {
    $.get("/api/authors").done(function (authors) {
        authors.forEach(function (author) {
            $("#book_author").append(`
                    <option value=${author.id}>
                        ${author.name} ${author.surname}
                    </option>
                `);
        });
        $("#book_author").val(author);
    });
}

function getGenres() {
    $.get("/api/genres").done(function (genres) {
        genres.forEach(function (genre) {
            $("#book_genre").append(`
                    <option value=${genre.id}>
                        ${genre.name}
                    </option>
                `);
        })
    });
}

function getGenresAndSelect(genre) {
    $.get("/api/genres").done(function (genres) {
        genres.forEach(function (genre) {
            $("#book_genre").append(`
                    <option value=${genre.id}>
                        ${genre.name}
                    </option>
                `);
        });
        $("#book_genre").val(genre);
    });
}

function goToBooksPage() {
    location.href = "/books";
}


function deleteBook(bookId) {
    $.ajax({
        url: "/books/" + bookId,
        method: "DELETE",
        success: function () {
            alert("Book with id " + bookId + " is successfully deleted!");
            goToBooksPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    })
}

function addBook() {
    const form = $("#add-book");
    const url = form.attr("action");
    const title = document.querySelector("#book_title").value;
    const author_id = document.querySelector("#book_author").value;
    const genre_id = document.querySelector("#book_genre").value;
    const book = {'id': null, 'title': title, 'authorDTO': {'id': author_id}, 'genreDTO': {'id': genre_id}};

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        data: JSON.stringify(book),
        contentType: "application/json;charset=utf-8",
        success: function () {
            alert("Book is successfully added!");
            goToBooksPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}

function updateBook() {
    const form = $("#edit-genre");
    const url = form.attr("action");
    const id = document.querySelector("#book_id").value;
    const title = document.querySelector("#book_title").value;
    const author_id = document.querySelector("#book_author").value;
    const genre_id = document.querySelector("#book_genre").value;
    const book = {'id': id, 'title': title, 'authorDTO': {'id': author_id}, 'genreDTO': {'id': genre_id}};

    $.ajax({
        url: url,
        contentType: "application/json;charset=utf-8",
        type: "PUT",
        dataType: "json",
        data: JSON.stringify(book),
        success: function () {
            alert("Book is successfully updated!");
            goToBooksPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}