package com.mukesh.internCapstoneProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateExistingTaskRequestDTO(
        @NotNull
        Long id,
        @NotBlank
        String taskType,
        @NotBlank
        String taskDescription,
        @NotBlank
        Integer taskPriority
) {
}
