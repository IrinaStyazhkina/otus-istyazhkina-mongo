function loadAllAuthorsData() {
    $.get("/api/authors").done(function (authors) {
        authors.forEach(function (author) {
            $("tbody").append(`
                    <tr class="table_row">
                        <td class="table_cell">${author.id}</td>
                        <td class="table_cell">${author.name}</td>
                        <td class="table_cell">${author.surname}</td>

                        <td class="table_cell">
                            <a class="table_edit-link" href="authors/${author.id}">Edit</a>
                        </td>
                        <td class="table_cell">
                            <button onclick="deleteAuthor('${author.id}')" class="delete_button" type="button">Delete</button>
                        </td>
                    </tr>
                `);
        })
    });
}

function loadAuthorData() {
    const path = window.location.pathname;
    const pathLastPart = path.substr(path.lastIndexOf("/") + 1);
    if (pathLastPart !== "add") {
        $.get("/api/authors/" + pathLastPart).done(function (author) {
            $(".form_wrapper").append(`
                <form action="/authors/${pathLastPart}" class="item_form" id="edit-author" method="put" onsubmit="updateAuthor(); return false">
                    <label class="item_label" for="author_id">ID</label>
                    <input class="item_input" type="text" id="author_id" name="id" disabled value="${author.id}">
                    <label class="item_label" for="author_new_name">Name</label>
                    <input class="item_input" id="author_new_name" name="name" value="${author.name}" required>
                    <label class="item_label" for="author_new_surname">Surname</label>
                    <input class="item_input" id="author_new_surname" name="name" value="${author.surname}" required>
                    <button class="item_btn" type="submit" id="save_new">Save</button>
                </form>
                `)
        })
    } else {
        $('.form_wrapper').append(`
            <form action="/authors/add" class="item_form" id="add-author" method="post" onsubmit="addAuthor(); return false;">
                <label class="item_label" for="author_name">Name</label>
                <input class="item_input" id="author_name" value="" name="name" required>
                <label class="item_label" for="author_surname">Surname</label>
                <input class="item_input" id="author_surname" value="" name="name" required>
                <button class="item_btn" type="submit" id="save_edited">Save</button>
            </form>
            `)
    }
}

function goToAuthorsPage() {
    location.href = "/authors";
}

function deleteAuthor(authorId) {
    $.ajax({
        url: "/authors/" + authorId,
        method: "DELETE",
        success: function () {
            alert("Author with id " + authorId + " is successfully deleted!");
            goToAuthorsPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    })
}

function addAuthor() {
    const form = $("#add-author");
    const url = form.attr("action");
    const name = document.querySelector("#author_name").value;
    const surname = document.querySelector("#author_surname").value;

    const author = {'id': null, 'name': name, 'surname': surname};

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        data: JSON.stringify(author),
        contentType: "application/json;charset=utf-8",
        success: function () {
            alert("Author is successfully added!");
            goToAuthorsPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}

function updateAuthor() {
    const form = $("#edit-author");
    const url = form.attr("action");
    const id = document.querySelector("#author_id").value;
    const name = document.querySelector("#author_new_name").value;
    const surname = document.querySelector("#author_new_surname").value;

    const author = {'id': id, 'name': name, 'surname': surname};

    $.ajax({
        url: url,
        contentType: "application/json;charset=utf-8",
        type: "PUT",
        dataType: "json",
        data: JSON.stringify(author),
        success: function () {
            alert("Author is successfully updated!");
            goToAuthorsPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}