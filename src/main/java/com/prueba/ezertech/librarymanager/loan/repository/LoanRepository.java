package com.prueba.ezertech.librarymanager.loan.repository;

import com.prueba.ezertech.librarymanager.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /* ===== Búsquedas ===== */

    List<Loan> findByBorrowerEmail(String email);

    List<Loan> findByReturnDateIsNull();

    void deleteAllByBook_Id(Long bookId);

    Optional<Loan> findByBook_IdAndReturnDateIsNull(Long bookId); 

    boolean existsByBook_IdAndReturnDateIsNull(Long bookId); 

   

    long countByReturnDateIsNull();

    @Query("""
        SELECT COUNT(l) FROM Loan l
        WHERE l.dueDate < CURRENT_DATE
          AND l.returnDate IS NULL
    """)
    long countOverdueLoans();

    /* ===== Préstamos vencidos ===== */

    @Query("""
        SELECT l FROM Loan l
        WHERE l.dueDate < CURRENT_DATE
          AND l.returnDate IS NULL
    """)
    List<Loan> findOverdueLoans();
}
