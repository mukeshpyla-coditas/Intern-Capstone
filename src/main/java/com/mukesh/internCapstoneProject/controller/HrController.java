package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.request.CreateTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UpdateExistingTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.CreateTaskResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.HrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
@Tag(name = "HR related APIs")
public class HrController {
    private final HrService hrService;

    @PostMapping("/create/tasks")
    public ResponseEntity<ApiResponse<CreateTaskResponseDTO>> createTask(@RequestBody @Valid CreateTaskRequestDTO request) {
        CreateTaskResponseDTO response = hrService.createTask(request);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "Task is created successfully",
                response
        );
    }

    @PatchMapping("/upadate/task")
    public ResponseEntity<ApiResponse<CreateTaskResponseDTO>> updateTask(@RequestBody @Valid UpdateExistingTaskRequestDTO request) {
        CreateTaskResponseDTO response = hrService.updateExistingTask(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "Task Details are upadated successfully",
                response
        );
    }

    // Upload materials related to company-policies
    @PostMapping("/upload/policy-document")
    public ResponseEntity<ApiResponse<String>> uploadPolicyDocument(@RequestParam(name = "documentType") String documentType, @RequestPart(name = "file")MultipartFile file) {
        String response = hrService.uploadDocument(documentType, file);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "Uploaded the policy-document successfully",
                response
        );
    }
}
