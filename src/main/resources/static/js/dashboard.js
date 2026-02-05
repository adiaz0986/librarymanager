fetch("/api/books/stats", { headers: authHeaders() })
    .then(r => r.json())
    .then(stats => {
        document.getElementById("stats").innerHTML = `
            <div class="row text-center">
                <div class="col">📚 Total: ${stats.totalBooks}</div>
                <div class="col">✅ Disponibles: ${stats.availableBooks}</div>
                <div class="col">📕 Prestados: ${stats.borrowedBooks}</div>
                <div class="col">⚠️ Vencidos: ${stats.overdueLoans}</div>
            </div>
        `;
    });
