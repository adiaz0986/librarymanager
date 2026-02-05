package com.prueba.ezertech.librarymanager.book.repository;

import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn); 

    List<Book> findByStatus(BookStatus status);

    List<Book> findByAuthorContainingIgnoreCase(String author);


    long countByStatus(BookStatus status);

    @Query("""
        SELECT b FROM Book b
        WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Book> searchByTitleOrAuthor(String keyword);
}
