package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record NotificationDTO(
        String title,
        String message
) {
}
