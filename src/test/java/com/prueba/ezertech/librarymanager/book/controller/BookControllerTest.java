package com.prueba.ezertech.librarymanager.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import com.prueba.ezertech.librarymanager.book.service.BookService;
import com.prueba.ezertech.librarymanager.security.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = BookController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("BookController - Tests de Integración")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book();
        validBook.setId(1L);
        validBook.setTitle("Test Book");
        validBook.setAuthor("Test Author");
        validBook.setIsbn("1234567890123");
        validBook.setPublicationYear(2024);
        validBook.setStatus(BookStatus.AVAILABLE);
    }

    @Test
    @DisplayName("POST /api/books retorna 201 Created")
    void createBook_validBook_returnsCreated() throws Exception {
      
        when(bookService.create(any(Book.class))).thenReturn(validBook);

        String bookJson = """
            {
                "title": "Test Book",
                "author": "Test Author",
                "isbn": "1234567890123",
                "publicationYear": 2024
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    @DisplayName("GET /api/books retorna lista de libros")
    void getAllBooks_returnsBookList() throws Exception {
        
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Second Book");
        book2.setAuthor("Another Author");
        book2.setIsbn("9876543210987");
        book2.setPublicationYear(2023);
        book2.setStatus(BookStatus.AVAILABLE);

        List<Book> books = Arrays.asList(validBook, book2);
        when(bookService.findAll()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[1].title").value("Second Book"));
    }
}