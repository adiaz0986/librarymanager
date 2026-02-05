package com.prueba.ezertech.librarymanager.loan.entity;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @NotBlank
    private String borrowerName;

    @Email
    private String borrowerEmail;

    @NotNull
    private LocalDate loanDate;

    private LocalDate dueDate;
    private LocalDate returnDate;

    public boolean isOverdue() {
        return returnDate == null && dueDate.isBefore(LocalDate.now());
    }
}