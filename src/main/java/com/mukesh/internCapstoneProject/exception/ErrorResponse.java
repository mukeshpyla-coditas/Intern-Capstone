package com.mukesh.internCapstoneProject.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;
}
