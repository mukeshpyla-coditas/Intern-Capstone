package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record CreateTaskResponseDTO(
        Integer taskPriority,
        String taskName,
        String taskDescription,
        String createdAt
) {
}
