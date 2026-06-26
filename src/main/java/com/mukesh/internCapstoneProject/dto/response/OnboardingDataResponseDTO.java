package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OnboardingDataResponseDTO(
        List<RoadmapResponseDTO> response
) {
}
