package com.mukesh.internCapstoneProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequestDTO(
        @NotBlank
        String taskType,
        @NotBlank
        String taskDescription,
        @NotNull
        Integer taskPriority
) {
}
