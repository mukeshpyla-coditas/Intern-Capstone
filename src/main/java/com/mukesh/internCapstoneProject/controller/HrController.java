package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.request.CreateTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UpdateExistingTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.CreateTaskResponseDTO;
import com.mukesh.internCapstoneProject.service.HrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
@Tag(name = "HR related APIs")
public class HrController {
    private final HrService hrService;

    @PostMapping("/create/tasks")
    public ResponseEntity<CreateTaskResponseDTO> createTask(@RequestBody @Valid CreateTaskRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hrService.createTask(request));
    }

    @PatchMapping("/upadate/task")
    public ResponseEntity<CreateTaskResponseDTO> updateTask(@RequestBody @Valid UpdateExistingTaskRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(hrService.updateExistingTask(request));
    }

    // Upload materials related to company-policies
}
