package com.prueba.ezertech.librarymanager.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ezertech.librarymanager.book.entity.Book;
import com.prueba.ezertech.librarymanager.book.entity.BookStatus;
import com.prueba.ezertech.librarymanager.loan.entity.Loan;
import com.prueba.ezertech.librarymanager.loan.repository.LoanRepository;
import com.prueba.ezertech.librarymanager.loan.service.LoanService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = LoanController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("LoanController - Tests de Integración")
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanService loanService;

    @MockBean
    private LoanRepository loanRepository;

    private Loan validLoan;
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890123");
        book.setStatus(BookStatus.BORROWED);

        validLoan = new Loan();
        validLoan.setId(1L);
        validLoan.setBook(book);
        validLoan.setBorrowerName("John Doe");
        validLoan.setBorrowerEmail("john@example.com");
        validLoan.setLoanDate(LocalDate.now());
        validLoan.setDueDate(LocalDate.now().plusDays(14));
        validLoan.setReturnDate(null);
    }

    @Test
    @DisplayName("POST /api/loans retorna 201 Created")
    void createLoan_validRequest_returnsCreated() throws Exception {
        // Arrange
        when(loanService.loanBook(anyLong(), anyString(), anyString()))
                .thenReturn(validLoan);

        String loanRequestJson = """
            {
                "bookId": 1,
                "borrowerName": "John Doe",
                "borrowerEmail": "john@example.com"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.borrowerName").value("John Doe"))
                .andExpect(jsonPath("$.data.borrowerEmail").value("john@example.com"))
                .andExpect(jsonPath("$.message").value("Préstamo creado exitosamente"));
    }

    @Test
    @DisplayName("GET /api/loans/active retorna préstamos activos")
    void getActiveLoans_returnsActiveLoansList() throws Exception {
        // Arrange
        Loan loan2 = new Loan();
        loan2.setId(2L);
        loan2.setBook(book);
        loan2.setBorrowerName("Jane Smith");
        loan2.setBorrowerEmail("jane@example.com");
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusDays(14));
        loan2.setReturnDate(null);

        List<Loan> activeLoans = Arrays.asList(validLoan, loan2);
        when(loanRepository.findByReturnDateIsNull()).thenReturn(activeLoans);

        // Act & Assert
        mockMvc.perform(get("/api/loans/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].borrowerName").value("John Doe"))
                .andExpect(jsonPath("$.data[1].borrowerName").value("Jane Smith"));
    }
}
