package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.request.DocumentDisapprovalMessageRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.FetchApprovalDocumentsResponseDTO;
import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.entity.Documents;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.ManagerApprovalStatus;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.global.PageResponse;
import com.mukesh.internCapstoneProject.repository.DocumentsRepository;
import com.mukesh.internCapstoneProject.repository.InternTasksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {
    private final TaskService taskService;
    private final InternService internService;
    private final DocumentsRepository documentsRepository;
    private final CommonServiceImpl commonService;
    private final MailService mailService;
    private final InternTaskService internTaskService;
    private final InternTasksRepository internTasksRepository;

    @Transactional
    public PageResponse<List<FetchInternsResponseDTO>> fetchAllInterns(Integer pageNo, Integer size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, size, sort);
        Page<Interns> internsList = internService.fetchAllInternsByManager(commonService.getExistingUser(), pageable);
        List<FetchInternsResponseDTO> response = new ArrayList<>();
        for(Interns intern : internsList.getContent()) {
            Users user = intern.getIntern();
            FetchInternsResponseDTO details = FetchInternsResponseDTO.builder()
                    .internId(intern.getId())
                    .internEmail(user.getEmail())
                    .internName(user.getFirstName() + " " + user.getLastName())
                    .onboardedHrName(intern.getOnboardedHrId().getFirstName() + " " + intern.getOnboardedHrId().getLastName())
                    .onboardingStatus(intern.getOnboardingStatus().name())
                    .build();
            response.add(details);
        }
        return PageResponse.<List<FetchInternsResponseDTO>>builder()
                .totalElements(internsList.getTotalElements())
                .totalPages(internsList.getTotalPages())
                .page(pageNo)
                .size(5)
                .isLastPage(internsList.isLast())
                .data(response)
                .build();
    }

    @Transactional
    public PageResponse<List<FetchApprovalDocumentsResponseDTO>> fetchAllDocumentsForApproval(Long internId, Integer page) {
        Interns requestedIntern = internService.getInternById(internId);
        Pageable pageable = PageRequest.of(page, 5);
        Page<Documents> documentsList = documentsRepository.findAllByInternIdAndRequireManagerApproval(requestedIntern, true, pageable);
        if(documentsList.isEmpty()) throw new InvalidRequestException("There are no documents related to the requested intern-" + requestedIntern.getId() + " that requires manager approval.");
        List<FetchApprovalDocumentsResponseDTO> response = new ArrayList<>();
        for(Documents document : documentsList.getContent()) {
            Users intern = document.getInternId().getIntern();
            FetchApprovalDocumentsResponseDTO details = FetchApprovalDocumentsResponseDTO.builder()
                    .internName(intern.getFirstName() + " " + intern.getLastName())
                    .documentType(document.getDocumentType().name())
                    .uploadedAt(String.valueOf(document.getUpdatedAt()))
                    .documentLocation(document.getDocumentLocation())
                    .build();
            response.add(details);
        }
        return PageResponse.<List<FetchApprovalDocumentsResponseDTO>>builder()
                .size(5)
                .totalPages(documentsList.getTotalPages())
                .totalElements(documentsList.getTotalElements())
                .isLastPage(documentsList.isLast())
                .data(response)
                .build();
    }

    @Transactional
    public String approveDocument(Long documentId) {
        Documents requestedDocument = documentsRepository.findById(documentId).orElseThrow(() -> new NotFoundException("Document with specified ID is not found."));
        if(requestedDocument.getRequireManagerApproval().equals(ManagerApprovalStatus.NOT_REQUIRED)) throw new InvalidRequestException("Requested Document does not require Manager approval.");
        requestedDocument.setRequireManagerApproval(ManagerApprovalStatus.APPROVED);
        mailService.sendMail(commonService.getExistingUser().getEmail(), requestedDocument.getInternId().getIntern().getEmail(), requestedDocument.getDocumentType() + " Document Approval Mail", "Your " + requestedDocument.getDocumentType() + " is approved by the manager. You may continue with your process.");
        log.info("There is a mail sent to the intern, regarding the approval of the document");
        documentsRepository.save(requestedDocument);
        return "The Document of type " + requestedDocument.getDocumentType() + " is marked as APPROVED by " + commonService.getExistingUser().getFirstName();
    }

    @Transactional
    public String disapproveDocument(DocumentDisapprovalMessageRequestDTO request) {
        Documents requestedDocument = documentsRepository.findById(request.documentId()).orElseThrow(() -> new NotFoundException("Document with specified ID is not found."));
        if(requestedDocument.getRequireManagerApproval().equals(ManagerApprovalStatus.NOT_REQUIRED)) throw new InvalidRequestException("Requested Document does not require Manager approval.");
        requestedDocument.setRequireManagerApproval(ManagerApprovalStatus.DISAPPROVED);

        mailService.sendMail(commonService.getExistingUser().getEmail(), requestedDocument.getInternId().getIntern().getEmail(), requestedDocument.getDocumentType() + " Document Disapproval Mail", request.reasonOfDisapproval());
        log.info("There is a mail sent to the intern, regarding the disapproval of the document");
        documentsRepository.save(requestedDocument);

        Tasks requestedTask = taskService.findTaskByTaskType(requestedDocument.getDocumentType().name());
        InternTasks internTask = internTaskService.findInternTaskByInternAndTaskType(requestedDocument.getInternId(), requestedTask);
        internTask.setTaskStatus(TaskStatus.ISSUE_RAISED);
        internTaskService.save(internTask);
        log.info("Marked the respective task of the intern as ISSUE RAISED.");

        return "The Document of type " + requestedDocument.getDocumentType() + " is marked as APPROVED by " + commonService.getExistingUser().getFirstName();
    }

}
