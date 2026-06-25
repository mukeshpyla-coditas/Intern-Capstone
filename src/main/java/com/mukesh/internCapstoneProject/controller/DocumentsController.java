package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.service.InternService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents related APIs")
public class DocumentsController {
    private final InternService internService;

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadPolicyDocument(@PathVariable Long documentId) {
        Resource resource = internService.downloadPolicyDocument(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
