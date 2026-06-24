package com.mukesh.internCapstoneProject.dto.request;

public record UpdateExistingTaskRequestDTO(
        Long id,
        String taskType,
        String taskDescription,
        Integer taskPriority
) {
}
