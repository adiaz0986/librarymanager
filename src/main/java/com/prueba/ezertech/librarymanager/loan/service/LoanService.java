package com.prueba.ezertech.librarymanager.loan.service;

import com.prueba.ezertech.librarymanager.book.entity.*;
import com.prueba.ezertech.librarymanager.book.repository.BookRepository;
import com.prueba.ezertech.librarymanager.loan.entity.Loan;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    /* ===== Prestar libro ===== */

    public Loan loanBook(Long bookId, String borrowerName, String borrowerEmail) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException(bookId);
        }

        book.setStatus(BookStatus.BORROWED);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrowerName(borrowerName);
        loan.setBorrowerEmail(borrowerEmail);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));

        return loanRepository.save(loan);
    }

    /* ===== Devolver libro ===== */

    public Loan returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getReturnDate() != null) {
            throw new BookAlreadyReturnedException(loanId);
        }

        Book book = loan.getBook();
        book.setStatus(BookStatus.AVAILABLE);

        loan.setReturnDate(LocalDate.now());
        return loanRepository.save(loan);
    }

    /* ===== Préstamo activo ===== */

    @Transactional(readOnly = true)
    public Loan getActiveLoanByBookId(Long bookId) {
        return loanRepository.findByBook_IdAndReturnDateIsNull(bookId)
                .orElseThrow(() -> new LoanNotFoundException(bookId));
    }

}