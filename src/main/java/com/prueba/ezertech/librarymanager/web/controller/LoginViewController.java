package com.prueba.ezertech.librarymanager.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}