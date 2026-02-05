package com.prueba.ezertech.librarymanager.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Year;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    @Size(min = 13, max = 13)
    @Column(unique = true)
    private String isbn;

    @Min(1000)
    private int publicationYear;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();


   
    @AssertTrue(message = "El año de publicación no puede ser mayor al año actual")
    public boolean isPublicationYearValid() {
        return publicationYear <= Year.now().getValue();
    }
}
