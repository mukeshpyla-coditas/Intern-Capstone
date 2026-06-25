package com.mukesh.internCapstoneProject.dto.request;

public record DocumentDisapprovalMessageRequestDTO(
        Long documentId,
        String reasonOfDisapproval
) {
}
