function loadAllGenresData() {
    $.get("/api/genres").done(function (genres) {
        genres.forEach(function (genre) {
            $("tbody").append(`
                    <tr class="table_row">
                        <td class="table_cell">${genre.id}</td>
                        <td class="table_cell">${genre.name}</td>
                        <td class="table_cell">
                            <a class="table_edit-link" href="genres/${genre.id}">Edit</a>
                        </td>
                        <td class="table_cell">
                            <button onclick="deleteGenre('${genre.id}')" class="delete_button" type="button">Delete</button>
                        </td>
                    </tr>
                `);
        })
    });
}

function loadGenreData() {
    const path = window.location.pathname;
    const pathLastPart = path.substr(path.lastIndexOf("/") + 1);
    if (pathLastPart !== "add") {
        $.get("/api/genres/" + pathLastPart).done(function (genre) {
            $(".form_wrapper").append(`
                <form action="/genres/${pathLastPart}" class="item_form" id="edit-genre" method="put" onsubmit="updateGenre(); return false">
                    <label class="item_label" for="genre_id">ID</label>
                    <input class="item_input" type="text" id="genre_id" name="id" disabled value="${genre.id}">
                    <label class="item_label" for="genre_new_name">Name</label>
                    <input class="item_input" id="genre_new_name" name="name" value="${genre.name}" required>
                    <button class="item_btn" type="submit" id="save_new">Save</button>
                </form>
                `)
        })
    } else {
        $('.form_wrapper').append(`
            <form action="/genres/add" class="item_form" id="add-genre" method="post" onsubmit="addGenre(); return false;">
                <label class="item_label" for="genre_name">Name</label>
                <input class="item_input" id="genre_name" value="" name="name" required>
                <button class="item_btn" type="submit" id="save_edited">Save</button>
            </form>
            `)
    }
}

function goToGenresPage() {
    location.href = "/genres";
}

function deleteGenre(genreId) {
    $.ajax({
        url: "/genres/" + genreId,
        method: "DELETE",
        success: function () {
            alert("Genre with id " + genreId + " is successfully deleted!");
            goToGenresPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    })
}

function addGenre() {
    const form = $("#add-genre");
    const url = form.attr("action");
    const name = document.querySelector("#genre_name").value;
    const genre = {'id': null, 'name': name};

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        data: JSON.stringify(genre),
        contentType: "application/json;charset=utf-8",
        success: function () {
            alert("Genre is successfully added!");
            goToGenresPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}

function updateGenre() {
    const form = $("#edit-genre");
    const url = form.attr("action");
    const id = document.querySelector("#genre_id").value;
    const name = document.querySelector("#genre_new_name").value;
    const genre = {'id': id, 'name': name};

    $.ajax({
        url: url,
        contentType: "application/json;charset=utf-8",
        type: "PUT",
        dataType: "json",
        data: JSON.stringify(genre),
        success: function () {
            alert("Genre is successfully updated!");
            goToGenresPage();
        },
        error: function (error) {
            alert(error.responseText);
        }
    });
}
