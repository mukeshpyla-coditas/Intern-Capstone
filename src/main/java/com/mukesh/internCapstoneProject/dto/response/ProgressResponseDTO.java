package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record ProgressResponseDTO(
        Long internId,
        String internName,
        String message,
        Double progress
) {
}
