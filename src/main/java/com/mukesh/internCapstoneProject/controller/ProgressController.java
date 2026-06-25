package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.response.ProgressResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.InternService;
import com.mukesh.internCapstoneProject.service.ProgressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
@Tag(name = "Progress related APIs")
public class ProgressController {
    private final InternService internService;

    @GetMapping("/{internId}")
    public ResponseEntity<ApiResponse<ProgressResponseDTO>> getProgress(@PathVariable Long internId) {
        ProgressResponseDTO response = internService.getProgress(internId);
        return ApiResponse.success(
                HttpStatus.OK,
                "Calculated and fetched the progress of the requested candidate",
                response
        );
    }
}
