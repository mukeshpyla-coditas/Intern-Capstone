package com.mukesh.internCapstoneProject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteUserRequestDTO(
        @Email
        @NotBlank
        String receiverMail,
        @NotBlank
        String mailSubject,
        @NotBlank
        String mailMessage
) {
}
