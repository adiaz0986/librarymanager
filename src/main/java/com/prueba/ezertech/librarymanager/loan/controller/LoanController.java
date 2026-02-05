package com.prueba.ezertech.librarymanager.loan.controller;

import com.prueba.ezertech.librarymanager.common.ApiResponse;
import com.prueba.ezertech.librarymanager.loan.entity.Loan;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.loan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Loans", description = "API para gestión de préstamos")
public class LoanController {

    private final LoanService loanService;
    private final LoanRepository loanRepository;

    
    public record LoanRequest(
            @NotNull(message = "El ID del libro es obligatorio")
            Long bookId,
            
            @NotBlank(message = "El nombre del solicitante es obligatorio")
            String borrowerName,
            
            @Email(message = "Debe proporcionar un email válido")
            @NotBlank(message = "El email es obligatorio")
            String borrowerEmail
    ) {}

    /* ===== Crear préstamo ===== */
    @PostMapping
    @Operation(summary = "Crear un nuevo préstamo")
    public ResponseEntity<ApiResponse<Loan>> createLoan(@Valid @RequestBody LoanRequest request) {
        Loan loan = loanService.loanBook(
                request.bookId(),
                request.borrowerName(),
                request.borrowerEmail()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(loan, "Préstamo creado exitosamente"));
    }

    /* ===== Devolver libro ===== */
    @PutMapping("/{id}/return")
    @Operation(summary = "Registrar devolución de un libro")
    public ResponseEntity<ApiResponse<Loan>> returnBook(@PathVariable Long id) {
        Loan loan = loanService.returnBook(id);
        return ResponseEntity.ok(
                ApiResponse.success(loan, "Libro devuelto exitosamente")
        );
    }

    /* ===== Listar todos los préstamos ===== */
    @GetMapping
    @Operation(summary = "Listar todos los préstamos")
    public ResponseEntity<ApiResponse<List<Loan>>> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(loans));
    }

    /* ===== Préstamos activos ===== */
    @GetMapping("/active")
    @Operation(summary = "Listar préstamos activos (no devueltos)")
    public ResponseEntity<ApiResponse<List<Loan>>> getActiveLoans() {
        List<Loan> activeLoans = loanRepository.findByReturnDateIsNull();
        return ResponseEntity.ok(ApiResponse.success(activeLoans));
    }

    /* ===== Préstamos vencidos ===== */
    @GetMapping("/overdue")
    @Operation(summary = "Listar préstamos vencidos")
    public ResponseEntity<ApiResponse<List<Loan>>> getOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans();
        return ResponseEntity.ok(ApiResponse.success(overdueLoans));
    }

    /* ===== Préstamos por email ===== */
    @GetMapping("/borrower/{email}")
    @Operation(summary = "Buscar préstamos por email del solicitante")
    public ResponseEntity<ApiResponse<List<Loan>>> getLoansByBorrowerEmail(@PathVariable String email) {
        List<Loan> loans = loanRepository.findByBorrowerEmail(email);
        return ResponseEntity.ok(ApiResponse.success(loans));
    }

    /* ===== Obtener préstamo por ID ===== */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un préstamo por ID")
    public ResponseEntity<ApiResponse<Loan>> getLoanById(@PathVariable Long id) {
        return loanRepository.findById(id)
                .map(loan -> ResponseEntity.ok(ApiResponse.success(loan)))
                .orElse(ResponseEntity.notFound().build());
    }
}