package com.example.moviehub.api.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@RestController // REST endpoint do pobierania logów
public class AdminLogRestController {

    @GetMapping("/api/admin/logs") // Zwraca ostatnie linie z pliku logs/app.log
    public List<String> tail(@RequestParam(defaultValue = "200") int lines) throws Exception {
        int max = Math.max(1, Math.min(lines, 2000)); // limit: min 1, max 2000 linii

        Path path = Path.of("logs", "app.log"); // ścieżka do logów
        File file = path.toFile();
        if (!file.exists()) {
            return List.of("Log file not found: " + path.toAbsolutePath()); // komunikat gdy brak pliku
        }

        // Przechowuje tylko ostatnie max linii (FIFO)
        Deque<String> dq = new ArrayDeque<>(max);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (dq.size() == max) dq.removeFirst(); // usuwa najstarszą linię
                dq.addLast(line); // dodaje najnowszą
            }
        }

        return dq.stream().toList(); // zwraca końcową listę linii
    }
}
