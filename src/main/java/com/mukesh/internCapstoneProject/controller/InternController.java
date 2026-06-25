package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.response.FetchAllTasksResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.InternService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/intern")
@RequiredArgsConstructor
@Tag(name = "Intern related APIs")
public class InternController {
    private final InternService internService;

    @GetMapping("/tasks/{internId}")
    public ResponseEntity<ApiResponse<List<FetchAllTasksResponseDTO>>> fetchAllTasks(
            @PathVariable @NotNull Long internId,
            @RequestParam(required = false, name = "taskStatus", defaultValue = "IN_PROGRESS") String taskStatus
    ) {
        List<FetchAllTasksResponseDTO> response = internService.fetchAllTasks(internId, taskStatus);
        return ApiResponse.success(
                HttpStatus.OK,
                "Fectched all the tasks based on the filter",
                response
        );
    }

    @PatchMapping("/submit/task/{taskId}")
    public ResponseEntity<ApiResponse<String>> submitTask(@PathVariable Long taskId) {
        String response = internService.submitTask(taskId);
        return ApiResponse.success(
                HttpStatus.OK,
                "Task is marked COMPLETED",
                response
        );
    }



    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadPolicyDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(internService.downloadPolicyDocument(documentId));
    }
}
