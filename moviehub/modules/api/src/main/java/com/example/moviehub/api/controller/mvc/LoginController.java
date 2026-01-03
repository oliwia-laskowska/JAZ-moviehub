package com.example.moviehub.api.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Kontroler MVC dla strony logowania
public class LoginController {

    @GetMapping("/login") // Widok logowania używany przez Spring Security
    public String login(
            @RequestParam(value = "error", required = false) String error,   // parametr gdy logowanie się nie udało
            @RequestParam(value = "logout", required = false) String logout, // parametr po poprawnym wylogowaniu
            org.springframework.ui.Model model
    ) {
        model.addAttribute("error", error != null);   // do pokazania komunikatu o błędzie
        model.addAttribute("logout", logout != null); // do pokazania komunikatu o wylogowaniu
        return "login"; // widok login.html
    }
}
