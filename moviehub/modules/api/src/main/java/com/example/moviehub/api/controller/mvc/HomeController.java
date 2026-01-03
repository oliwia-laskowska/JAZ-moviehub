package com.example.moviehub.api.controller.mvc;

import com.example.moviehub.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Kontroler MVC dla stron
public class HomeController {

    private final MovieService movieService; // Serwis do pobierania listy filmów

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/") // Strona główna
    public String index() {
        return "index"; // widok index.html
    }

    @GetMapping("/movies") // Strona z listą filmów
    public String movies(Model model) {
        model.addAttribute("movies", movieService.list()); // przekazuje filmy do szablonu
        return "movies"; // widok movies.html
    }
}
