package com.prueba.ezertech.librarymanager.exception;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(Long bookId) {
        super("El libro no está disponible para préstamo. ID del libro: " + bookId);
    }
}
