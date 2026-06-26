package com.mukesh.internCapstoneProject.dto.response;

import lombok.Builder;

@Builder
public record RoadmapResponseDTO(
        Integer taskPriority,
        String taskName,
        String taskDescription,
        String taskDeadline
) {
}
