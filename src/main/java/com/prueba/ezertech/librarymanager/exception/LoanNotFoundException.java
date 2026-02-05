package com.prueba.ezertech.librarymanager.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long loanId) {
        super("No se encontró el préstamo con ID: " + loanId);
    }
}
