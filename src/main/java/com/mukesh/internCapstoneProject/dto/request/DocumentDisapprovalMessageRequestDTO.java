package com.mukesh.internCapstoneProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentDisapprovalMessageRequestDTO(
        @NotNull
        Long documentId,
        @NotBlank
        String reasonOfDisapproval
) {
}
