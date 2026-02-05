package com.prueba.ezertech.librarymanager.book.controller;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    /* ===== Crear libro ===== */
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody @Valid Book book) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.create(book));
    }

    /* ===== Editar libro ===== */
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable Long id,
            @RequestBody @Valid Book book
    ) {
        return ResponseEntity.ok(bookService.update(id, book));
    }

    /* ===== Listar libros ===== */
    @GetMapping
    public ResponseEntity<List<Book>> list() {
        return ResponseEntity.ok(bookService.findAll());
    }

    /* ===== Buscar libros ===== */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(bookService.search(q));
    }

    /* ===== Estadísticas ===== */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(bookService.getStatistics());
    }

    /* ===== Eliminar libro ===== */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
