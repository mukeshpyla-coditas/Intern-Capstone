package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.response.FetchAllTasksResponseDTO;
import com.mukesh.internCapstoneProject.service.InternService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/intern")
@RequiredArgsConstructor
@Tag(name = "Intern related APIs")
public class InternController {
    private final InternService internService;

    @GetMapping("/tasks/{internId}")
    public ResponseEntity<List<FetchAllTasksResponseDTO>> fetchAllTasks(@PathVariable @NotNull Long internId) {
        return ResponseEntity.ok(internService.fetchAllTasks(internId));
    }
}
