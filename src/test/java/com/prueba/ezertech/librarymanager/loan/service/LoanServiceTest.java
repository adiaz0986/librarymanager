package com.prueba.ezertech.librarymanager.loan.service;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import com.prueba.ezertech.librarymanager.book.repository.BookRepository;
import com.prueba.ezertech.librarymanager.loan.entity.Loan;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.exception.BookNotAvailableException;
import com.prueba.ezertech.librarymanager.exception.BookAlreadyReturnedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoanService - Tests Unitarios")
class LoanServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private Book availableBook;
    private Book borrowedBook;
    private Loan activeLoan;

    @BeforeEach
    void setUp() {
        availableBook = new Book();
        availableBook.setId(1L);
        availableBook.setTitle("Available Book");
        availableBook.setAuthor("Author");
        availableBook.setIsbn("1234567890123");
        availableBook.setStatus(BookStatus.AVAILABLE);

        borrowedBook = new Book();
        borrowedBook.setId(2L);
        borrowedBook.setTitle("Borrowed Book");
        borrowedBook.setAuthor("Author");
        borrowedBook.setIsbn("9876543210987");
        borrowedBook.setStatus(BookStatus.BORROWED);

        activeLoan = new Loan();
        activeLoan.setId(1L);
        activeLoan.setBook(borrowedBook);
        activeLoan.setBorrowerName("John Doe");
        activeLoan.setBorrowerEmail("john@example.com");
        activeLoan.setLoanDate(LocalDate.now());
        activeLoan.setDueDate(LocalDate.now().plusDays(14));
        activeLoan.setReturnDate(null);
    }

    @Test
    @DisplayName("Prestar libro disponible debe funcionar")
    void loanBook_availableBook_success() {
        // Arrange
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(availableBook));
        when(loanRepository.save(any(Loan.class)))
                .thenReturn(activeLoan);

        // Act
        Loan result = loanService.loanBook(1L, "John Doe", "john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(BookStatus.BORROWED, availableBook.getStatus());
        verify(bookRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Prestar libro no disponible debe fallar")
    void loanBook_unavailableBook_throwsException() {
        // Arrange
        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(borrowedBook));

        // Act & Assert
        assertThrows(BookNotAvailableException.class, () -> {
            loanService.loanBook(2L, "Jane Doe", "jane@example.com");
        });

        verify(bookRepository, times(1)).findById(2L);
        verify(loanRepository, never()).save(any(Loan.class));
    }
}
