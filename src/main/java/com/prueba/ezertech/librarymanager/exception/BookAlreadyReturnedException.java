package com.prueba.ezertech.librarymanager.exception;

public class BookAlreadyReturnedException extends RuntimeException {

    public BookAlreadyReturnedException(Long loanId) {
        super("El libro correspondiente al préstamo ya fue devuelto. ID del préstamo: " + loanId);
    }
}
