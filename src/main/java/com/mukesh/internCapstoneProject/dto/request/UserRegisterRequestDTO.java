package com.mukesh.internCapstoneProject.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public record UserRegisterRequestDTO(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 6, message = "password must be minimum of 6 characters")
        String password,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 10, max = 10, message = "contact-number must be of 10 digits.")
        @NumberFormat(style = NumberFormat.Style.NUMBER)
        String contactNumber
) {
}
