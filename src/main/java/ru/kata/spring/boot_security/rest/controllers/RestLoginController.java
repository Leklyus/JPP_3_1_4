package ru.kata.spring.boot_security.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestLoginController {

    @GetMapping(value = {"/", "/index"})
    public String showPage() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
