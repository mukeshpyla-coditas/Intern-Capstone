package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record UserLoginResponseDTO(
        String accessToken,
        String refreshToken,
        String userRole,
        String message
) {
}
