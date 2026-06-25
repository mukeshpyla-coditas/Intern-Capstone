package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.entity.PolicyDocuments;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import com.mukesh.internCapstoneProject.enums.Roles;
import com.mukesh.internCapstoneProject.exception.FileCreationException;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.PolicyDocumentsRepository;
import com.mukesh.internCapstoneProject.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class DocumentUploadService {
    private final String hrFileUploadDir;
    private final PolicyDocumentsRepository policyDocumentsRepository;
    private final CommonServiceImpl commonService;

    DocumentUploadService(
            @Value("${file.upload-dir}")
            String hrFileUploadDir,
            PolicyDocumentsRepository policyDocumentsRepository,
            CommonServiceImpl commonService
    ) {
        this.hrFileUploadDir = hrFileUploadDir;
        this.policyDocumentsRepository = policyDocumentsRepository;
        this.commonService = commonService;
    }

    public String uploadDocument(DocumentType documentType, MultipartFile file) {
        Users existingUser = commonService.getExistingUser();
        if(!existingUser.getRole().equals(Roles.HR)) throw new InvalidRequestException("Only HR can upload the policy-documents.");

        if(file.isEmpty()) throw new InvalidRequestException("The file uploaded is empty. Please re-verify and try again.");
        Path folderPath = Paths.get(hrFileUploadDir);
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            throw new FileCreationException("");
        }
        // Gives me the actual extension of the file
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = documentType.name() + "_" + UUID.randomUUID().toString();
        try {
            Files.copy(file.getInputStream(), folderPath);
        } catch (IOException e) {
            throw new FileCreationException("Error while creating the copy the file locally.");
        }
        PolicyDocuments documents = PolicyDocuments.builder()
                .fileName(fileName)
                .documentType(documentType)
                .fileExtension(extension)
                .fileLocation(folderPath.toString())
                .uploadedBy(existingUser)
                .build();
        policyDocumentsRepository.save(documents);
        log.info("A new document is stored");

        return "A new policy document has been created in the path: " + folderPath.toString();
    }
}
