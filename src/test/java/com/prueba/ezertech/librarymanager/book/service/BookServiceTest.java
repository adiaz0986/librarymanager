package com.prueba.ezertech.librarymanager.book.service;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import com.prueba.ezertech.librarymanager.book.repository.BookRepository;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.exception.DuplicateIsbnException;
import com.prueba.ezertech.librarymanager.exception.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService - Tests Unitarios")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private BookService bookService;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book();
        validBook.setId(1L);
        validBook.setTitle("Test Book");
        validBook.setAuthor("Test Author");
        validBook.setIsbn("1234567890123");
        validBook.setPublicationYear(2024);
        validBook.setStatus(BookStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Registrar libro con ISBN duplicado debe fallar")
    void registerBook_duplicateIsbn_throwsException() {
        // Arrange
        when(bookRepository.existsByIsbn("1234567890123"))
                .thenReturn(true);

        Book newBook = new Book();
        newBook.setIsbn("1234567890123");
        newBook.setTitle("Duplicate Book");
        newBook.setAuthor("Author");
        newBook.setPublicationYear(2024);

        // Act & Assert
        assertThrows(DuplicateIsbnException.class, () -> {
            bookService.create(newBook);
        });

        verify(bookRepository, times(1)).existsByIsbn("1234567890123");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Buscar libro por ID inexistente debe lanzar excepción")
    void findById_nonExistentId_throwsException() {
        // Arrange
        Long nonExistentId = 999L;
        when(bookRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            bookService.findById(nonExistentId);
        });

        verify(bookRepository, times(1)).findById(nonExistentId);
    }
}