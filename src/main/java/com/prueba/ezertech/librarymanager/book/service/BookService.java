package com.prueba.ezertech.librarymanager.book.service;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import com.prueba.ezertech.librarymanager.book.repository.BookRepository;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    /* ===== Crear libro ===== */

    public Book create(Book book) {
        book.setIsbn(book.getIsbn().trim());

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }

        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    /* ===== Editar libro ===== */

    public Book update(Long id, Book book) {
        Book existing = findById(id);

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setPublicationYear(book.getPublicationYear());

        return bookRepository.save(existing);
    }

    /* ===== Obtener todos ===== */

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /* ===== Buscar por ID ===== */

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /* ===== Buscar ===== */

    @Transactional(readOnly = true)
    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    /* ===== Estadísticas ===== */

    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalBooks", bookRepository.count());
        stats.put("availableBooks", bookRepository.countByStatus(BookStatus.AVAILABLE));
        stats.put("borrowedBooks", bookRepository.countByStatus(BookStatus.BORROWED));
        stats.put("activeLoans", loanRepository.countByReturnDateIsNull());
        stats.put("overdueLoans", loanRepository.countOverdueLoans());
        return stats;
    }

    /* ===== Eliminar ===== */

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        loanRepository.deleteAllByBook_Id(id);
        bookRepository.deleteById(id);
    }
}