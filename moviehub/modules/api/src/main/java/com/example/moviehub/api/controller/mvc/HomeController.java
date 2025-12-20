package com.example.moviehub.api.controller.mvc;

import com.example.moviehub.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MovieService movieService;

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("movies", movieService.list());
        return "movies";
    }
}
