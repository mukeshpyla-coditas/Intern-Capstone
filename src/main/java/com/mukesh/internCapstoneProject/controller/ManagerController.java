package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.request.DocumentDisapprovalMessageRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.FetchApprovalDocumentsResponseDTO;
import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.global.PageResponse;
import com.mukesh.internCapstoneProject.service.ManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
@Tag(name = "Manager related APIs")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/interns")
    public ResponseEntity<PageResponse<List<FetchInternsResponseDTO>>> fetchInternsDetails(
            @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @RequestParam(required = false, name = "size", defaultValue = "5") Integer size,
            @RequestParam(required = false, name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(required = false, name = "sortOrder", defaultValue = "ASC") String sortOrder

    ) {
        return ResponseEntity.ok(managerService.fetchAllInterns(page, size, sortBy, sortOrder));
    }

    @GetMapping("/approval/documents")
    public ResponseEntity<PageResponse<List<FetchApprovalDocumentsResponseDTO>>> fetchAllDocumentsForApproval(@RequestParam(name = "internId") @NotNull Long internId, @RequestParam(name = "page") @NotNull Integer page) {
        return ResponseEntity.ok(managerService.fetchAllDocumentsForApproval(internId, page));
    }

    @PatchMapping("/approve/document/{documentId}")
    public ResponseEntity<ApiResponse<String>> approveDocumentOfIntern(@PathVariable @NotNull Long documentId) {
        String response = managerService.approveDocument(documentId);
        return ApiResponse.success(
                HttpStatus.OK,
                "Marked the requested document as APPROVED",
                response
        );
    }

    @PatchMapping("/diapprove/document")
    public ResponseEntity<ApiResponse<String>> disapproveDocumentOfIntern(@RequestBody @Valid DocumentDisapprovalMessageRequestDTO request) {
        String response = managerService.disapproveDocument(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "A disapproval mail has been successfully sent to the Intern",
                response
        );
    }
}
