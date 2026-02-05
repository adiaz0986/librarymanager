package com.prueba.ezertech.librarymanager.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loans")
public class LoanViewController {

      @GetMapping
    public String list() {
        return "loans/loans";
    }


}
