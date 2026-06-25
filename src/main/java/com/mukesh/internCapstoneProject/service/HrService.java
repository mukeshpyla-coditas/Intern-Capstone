package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.request.CreateTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.DocumentDisapprovalMessageRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UpdateExistingTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.CreateTaskResponseDTO;
import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.entity.Documents;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import com.mukesh.internCapstoneProject.enums.HrApprovalStatus;
import com.mukesh.internCapstoneProject.enums.ManagerApprovalStatus;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.global.PageResponse;
import com.mukesh.internCapstoneProject.repository.DocumentsRepository;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HrService {
    private final TaskService taskService;
    private final DocumentsRepository documentsRepository;
    private final DocumentService documentUploadService;
    private final CommonServiceImpl commonService;
    private final MailService mailService;
    private final InternTaskService internTaskService;
    private final InternsRepository internsRepository;
    private final InternService internService;

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

    public String approveDocument(Long documentId) {
        Documents requestedDocument = getDocument(documentId);
        requestedDocument.setHrApprovalStatus(HrApprovalStatus.APPROVED);
        log.info("Document has been marked as APPROVED by {}.", commonService.getExistingUser().getFirstName());

        String message = null;
        if(requestedDocument.getRequireManagerApproval().equals(ManagerApprovalStatus.MARKED_FOR_APPROVAL)) message = "The document " + requestedDocument.getDocumentType().name() + " is approved by the HR-team. Awaiting the Manager approval. Thank you.";
        else if(requestedDocument.getRequireManagerApproval().equals(ManagerApprovalStatus.NOT_REQUIRED)) message = "The document" + requestedDocument.getDocumentType().name() +  " is approved by the HR-team. Thank you.";
        mailService.sendMail(commonService.getExistingUser().getEmail(), requestedDocument.getInternId().getIntern().getEmail(), requestedDocument.getDocumentType().name() + " Document Approval Mail", message);
        log.info("A success mail has been sent to the intern as per the approval status.");

        if(requestedDocument.getHrApprovalStatus().equals(HrApprovalStatus.APPROVED) && requestedDocument.getRequireManagerApproval().equals(ManagerApprovalStatus.APPROVED)) {
            Tasks requestedTask = taskService.findTaskByTaskType(requestedDocument.getDocumentType().name());
            InternTasks internTask = internTaskService.findInternTaskByInternAndTaskType(requestedDocument.getInternId(), requestedTask);
            internTask.setTaskStatus(TaskStatus.COMPLETED);
            internTask.setCompletedAt(LocalDateTime.now());
            internTaskService.save(internTask);
            log.info("As the Document is approved by both HR and Manager, the respective Task of respective intern is marked as COMPLETED.");
        }

        boolean checkForCompletion = internService.checkIfAllTasksCompleted(requestedDocument.getInternId());
        if(checkForCompletion) {
            mailService.sendMail(commonService.getExistingUser().getEmail(), requestedDocument.getInternId().getIntern().getEmail(), "ONBOARDING COMPLETION MAIL", "Congratulations!! You have successfully completed your onboarding process by completing all tasks.");
            log.info("As the intern has completed all the tasks assigned, intern received mail regarding the successful onboarding.");
        }

        return "Document is marked as APPROVED by the HR: " + commonService.getExistingUser().getFirstName();
    }

    public String disapproveDocument(DocumentDisapprovalMessageRequestDTO request) {
        Documents document = getDocument(request.documentId());
        document.setHrApprovalStatus(HrApprovalStatus.ISSUE_RAISED);
        Users hr = commonService.getExistingUser();

        Tasks requestedTask = taskService.findTaskByTaskType(document.getDocumentType().name());
        InternTasks internTask = internTaskService.findInternTaskByInternAndTaskType(document.getInternId(), requestedTask);
        internTask.setTaskStatus(TaskStatus.ISSUE_RAISED);
        internTaskService.save(internTask);
        log.info("Status of the intern task has been changed to {} by the HR", TaskStatus.ISSUE_RAISED.name());

        Users intern = document.getInternId().getIntern();
        mailService.sendMail(hr.getEmail(), intern.getEmail(), document.getDocumentType() + " Document Disapproval Mail", request.reasonOfDisapproval());
        log.info("An Issue-Raised mail has been sent to the intern {} by the HR {}", intern.getFirstName(), hr.getFirstName());

        return "The requested document of ID-" + document.getId() + " is marked for disapproval. By HR: " + hr.getFirstName();
    }

    public Documents getDocument(Long documentId) {
        Documents requestedDocument = documentsRepository.findById(documentId).orElseThrow(() -> new NotFoundException(("Document with specified ID is not found.")));
        if(requestedDocument.getHrApprovalStatus().equals(HrApprovalStatus.APPROVED)) throw new InvalidRequestException("The requested document is already approved.");
        return requestedDocument;
    }

    public PageResponse<List<FetchInternsResponseDTO>> fetchDetailsOfInterns(Integer page, Integer size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Interns> internsPage = internsRepository.findAll(pageable);
        List<FetchInternsResponseDTO> response = new ArrayList<>();
        for(Interns intern : internsPage.getContent()) {
            Users user = intern.getIntern();
            FetchInternsResponseDTO details = FetchInternsResponseDTO.builder()
                    .internName(user.getFirstName() + " " + user.getLastName())
                    .internEmail(user.getEmail())
                    .internId(intern.getId())
                    .onboardedHrName(intern.getOnboardedHrId().getFirstName() + " " + intern.getOnboardedHrId().getLastName())
                    .onboardingStatus(intern.getOnboardingStatus().name())
                    .build();
            response.add(details);
        }
        return PageResponse.<List<FetchInternsResponseDTO>>builder()
                .totalElements(internsPage.getTotalElements())
                .totalPages(internsPage.getTotalPages())
                .page(page)
                .size(size)
                .isLastPage(internsPage.isLast())
                .data(response)
                .build();
    }
}
