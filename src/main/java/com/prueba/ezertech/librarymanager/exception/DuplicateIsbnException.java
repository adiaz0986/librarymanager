package com.prueba.ezertech.librarymanager.exception;

public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("Ya existe un libro registrado con el ISBN: " + isbn);
    }
}