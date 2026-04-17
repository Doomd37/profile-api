package com.myproject.profile_api.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {

        return ResponseEntity.status(400).body(
                Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {

        return ResponseEntity.status(404).body(
                Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApi(ExternalApiException ex) {

        return ResponseEntity.status(502).body(
                Map.of(
                        "status", "error",
                        "message", ex.getMessage() + " returned an invalid response"
                )
        );
    }

}