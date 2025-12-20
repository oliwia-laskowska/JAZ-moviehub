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

@RestController
public class AdminLogRestController {

    @GetMapping("/api/admin/logs")
    public List<String> tail(@RequestParam(defaultValue = "200") int lines) throws Exception {
        int max = Math.max(1, Math.min(lines, 2000));
        Path path = Path.of("logs", "app.log");
        File file = path.toFile();
        if (!file.exists()) {
            return List.of("Log file not found: " + path.toAbsolutePath());
        }

        Deque<String> dq = new ArrayDeque<>(max);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (dq.size() == max) dq.removeFirst();
                dq.addLast(line);
            }
        }
        return dq.stream().toList();
    }
}
