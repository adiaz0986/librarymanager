package com.prueba.ezertech.librarymanager.exception.handler;

import com.prueba.ezertech.librarymanager.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("BOOK_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<?> handleBookNotAvailable(BookNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("BOOK_NOT_AVAILABLE", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<?> handleDuplicateIsbn(DuplicateIsbnException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("DUPLICATE_ISBN", ex.getMessage()));
    }

    @ExceptionHandler(BookAlreadyReturnedException.class)
    public ResponseEntity<?> handleBookAlreadyReturned(BookAlreadyReturnedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("BOOK_ALREADY_RETURNED", ex.getMessage()));
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<?> handleLoanNotFound(LoanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("LOAN_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "VALIDATION_ERROR",
                        "details", ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                                .toList()
                )
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGeneric(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error("BAD_REQUEST", ex.getMessage()));
    }

    private Map<String, Object> error(String code, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "error", code,
                "message", message
        );
    }
}
