package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.PolicyDocuments;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import com.mukesh.internCapstoneProject.enums.Roles;
import com.mukesh.internCapstoneProject.exception.DataAccessException;
import com.mukesh.internCapstoneProject.exception.FileCreationException;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import com.mukesh.internCapstoneProject.repository.PolicyDocumentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class DocumentService {
    private final String hrFileUploadDir;
    private final String hrFileUploadFolder;
    private final String internFileUploadDir;
    private final PolicyDocumentsRepository policyDocumentsRepository;
    private final CommonServiceImpl commonService;
    private final InternsRepository internsRepository;

    DocumentService(
            @Value("${file.upload-dir}")
            String hrFileUploadDir,
            @Value("${file.upload-folder}")
            String hrFileUploadFolder,
            @Value("${file.intern.upload-dir}")
            String internFileUploadDir,
            PolicyDocumentsRepository policyDocumentsRepository,
            CommonServiceImpl commonService,
            InternsRepository internsRepository
    ) {
        this.hrFileUploadDir = hrFileUploadDir;
        this.policyDocumentsRepository = policyDocumentsRepository;
        this.commonService = commonService;
        this.hrFileUploadFolder = hrFileUploadFolder;
        this.internFileUploadDir = internFileUploadDir;
        this.internsRepository = internsRepository;
    }

    public String uploadPolicyDocument(DocumentType documentType, MultipartFile file) {
        Users existingUser = commonService.getExistingUser();
        if(!existingUser.getRole().equals(Roles.HR)) throw new InvalidRequestException("Only HR can upload the policy-documents.");

        if(file.isEmpty()) throw new InvalidRequestException("The file uploaded is empty. Please re-verify and try again.");
        Path folderPath = Paths.get(hrFileUploadDir, hrFileUploadFolder);

        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            throw new FileCreationException("Exception while creating folder-path.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = documentType.name() + "_" + UUID.randomUUID() + "." + extension;
        Path filePath = folderPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileCreationException("Error while creating the copy of the file locally.");
        }
        try {
            PolicyDocuments documents = PolicyDocuments.builder()
                    .documentType(documentType)
                    .fileName(fileName)
                    .fileExtension(extension)
                    .fileLocation(folderPath.toString())
                    .uploadedBy(existingUser)
                    .build();
            policyDocumentsRepository.save(documents);
            log.info("A new document is stored");
        } catch (JpaSystemException exception) {
            log.error("There was an internal error while uploading the details of the file.");
            throw new DataAccessException("Internal upload error.");
        }

        return "A new policy document has been created in the path: " + folderPath.toString();
    }

    public String uploadDocuments(DocumentType documentType, MultipartFile file) {
        Users existingUser = commonService.getExistingUser();
        if(!existingUser.getRole().equals(Roles.NEW_HIRE)) throw new InvalidRequestException("Only Interns can upload intern-related documents.");
        Interns requestedIntern = internsRepository.findByIntern(existingUser).orElseThrow(() -> new NotFoundException("Intern with specified User-credentials is not found."));

        if(file.isEmpty()) throw new InvalidRequestException("The file uploaded is empty. Please re-verify and try again.");
        String folderName = "intern-" + requestedIntern.getId();
        Path folderPath = Paths.get(internFileUploadDir, folderName);
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            throw new FileCreationException("Exception while creating and storing folder-path.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = documentType.name() + "_" + UUID.randomUUID() + "." + extension;
        Path filePath = folderPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileCreationException("Error while creating a copy of the file locally.");
        }

        return filePath.toString();
    }

    public Resource downloadFiles(Long documentId) {
        PolicyDocuments documents = policyDocumentsRepository.findById(documentId).orElseThrow(() -> new NotFoundException("Policy Document with specified ID, does not exist."));
        Path path = Paths.get(documents.getFileLocation());
        try {
            Resource resource = new UrlResource(path.toUri());
            if(!resource.exists()) throw new InvalidRequestException("No resource available with specified document's location.");
            return resource;
        } catch (MalformedURLException e) {
            throw new InvalidRequestException("The document's location is not valid. Please verify and try again.");
        }
    }
}
