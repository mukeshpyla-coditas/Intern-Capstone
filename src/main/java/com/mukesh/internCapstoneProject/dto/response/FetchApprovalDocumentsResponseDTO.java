package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record FetchApprovalDocumentsResponseDTO(
       String documentType,
       String internName,
       String uploadedAt,
       String documentLocation
) {
}
