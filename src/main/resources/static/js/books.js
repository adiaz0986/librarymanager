function loadBooks() {
    fetch("/api/books", { headers: authHeaders() })
        .then(r => r.json())
        .then(data => {
            let html = `<table class="table table-bordered">
                <tr><th>ID</th><th>Título</th><th>Autor</th><th>Estado</th></tr>`;
            data.forEach(b => {
                html += `<tr>
                    <td>${b.id}</td>
                    <td>${b.title}</td>
                    <td>${b.author}</td>
                    <td>${b.status}</td>
                </tr>`;
            });
            html += "</table>";
            document.getElementById("booksTable").innerHTML = html;
        });
}

loadBooks();
