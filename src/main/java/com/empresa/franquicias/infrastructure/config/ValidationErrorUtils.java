package com.empresa.franquicias.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public final class ValidationErrorUtils {
    private ValidationErrorUtils() {}

    public static ValidationErrorBuilder fromBindingErrors(BindingResult errors) {
        List<String> details = errors.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ValidationErrorBuilder.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .details(details)
                .build();
    }
}
