package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record FetchInternsResponseDTO(
        Long internId,
        String internName,
        String internEmail,
        String onboardedHrName,
        String onboardingStatus
) {
}
