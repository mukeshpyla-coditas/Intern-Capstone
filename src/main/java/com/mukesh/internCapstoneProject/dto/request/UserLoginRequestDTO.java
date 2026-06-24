package com.mukesh.internCapstoneProject.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
