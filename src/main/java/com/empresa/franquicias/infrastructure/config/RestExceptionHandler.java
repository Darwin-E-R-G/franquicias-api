package com.empresa.franquicias.infrastructure.config;

import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ValidationErrorBuilder> handleNotFoundException(ResourceNotFoundException ex) {
        ValidationErrorBuilder error = ValidationErrorBuilder.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .details(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorBuilder> handleValidationException(MethodArgumentNotValidException ex) {
        ValidationErrorBuilder error = ValidationErrorUtils.fromBindingErrors(ex.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ValidationErrorBuilder> handleIllegalArgumentException(IllegalArgumentException ex) {
        ValidationErrorBuilder error = ValidationErrorBuilder.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .details(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorBuilder> handleGenericException(Exception ex) {
        ValidationErrorBuilder error = ValidationErrorBuilder.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .details(List.of(ex.getMessage() != null ? ex.getMessage() : "Unexpected error"))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
