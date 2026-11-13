package com.empresa.franquicias.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorBuilder {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> details;
}
