package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record FetchAllTasksResponseDTO(
        Integer taskPriority,
        String taskType,
        String taskDescription,
        String deadline
) {
}
