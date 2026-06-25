package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.request.CreateTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UpdateExistingTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.CreateTaskResponseDTO;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class HrService {
    private final TaskService taskService;
    private final DocumentService documentUploadService;
    private final CommonServiceImpl commonService;

    public CreateTaskResponseDTO createTask(CreateTaskRequestDTO request) {
        return taskService.createTask(request);
    }

    public CreateTaskResponseDTO updateExistingTask(UpdateExistingTaskRequestDTO request) {
        return taskService.updateExistingTask(request);
    }

    public String uploadDocument(String documentType, MultipartFile file) {
        DocumentType requestedDocumentType = commonService.checkDocumentType(documentType);
        return documentUploadService.uploadPolicyDocument(requestedDocumentType, file);
    }
}
