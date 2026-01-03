package com.example.moviehub.common.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice // Globalna obsługa wyjątków dla REST API
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class) // Mapuje IllegalArgumentException na 400
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(
                        Instant.now(),             // timestamp błędu
                        400,                       // status HTTP
                        "Bad Request",             // nazwa błędu
                        ex.getMessage(),           // komunikat wyjątku
                        req.getRequestURI()        // endpoint, na którym poleciało
                )
        );
    }

    @ExceptionHandler(Exception.class) // Fallback na wszystkie inne wyjątki -> 500
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(
                        Instant.now(),
                        500,
                        "Internal Server Error",
                        ex.getMessage(),
                        req.getRequestURI()
                )
        );
    }
}
