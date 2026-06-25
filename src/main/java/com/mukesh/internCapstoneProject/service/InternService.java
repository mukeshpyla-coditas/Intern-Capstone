package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.FetchAllTasksResponseDTO;
import com.mukesh.internCapstoneProject.entity.Documents;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Invitations;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.OnboardingStatus;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternService {
    private final CommonServiceImpl commonService;
    private final InternsRepository internsRepository;
    private final TaskService taskService;
    private final InternTaskService internTaskService;
    private final DocumentService documentService;

    public void saveIntern(Users intern, Invitations invitation) {
        Interns newIntern = Interns.builder()
                .intern(intern)
                .managerId(invitation.getManager())
                .onboardedHrId(invitation.getSentBy())
                .onboardingStatus(OnboardingStatus.IN_PROGRESS)
                .build();
        internsRepository.save(newIntern);
        log.info("A new intern record is generated.");

        createTasks(newIntern);
    }

    public void createTasks(Interns intern) {
        List<Tasks> tasksList = taskService.getAllTasks();
        tasksList.forEach(task -> internTaskService.createInternTask(intern, task));
        log.info("All required tasks are mapped to the intern.");
    }

    public List<FetchAllTasksResponseDTO> fetchAllTasks(Long internId, String taskStatus) {
        Interns requestedIntern = internsRepository.findById(internId).orElseThrow(() -> new NotFoundException("Intern with the specified ID is not found."));
        TaskStatus requestedTaskStatus = commonService.checkTaskStatus(taskStatus);
        List<InternTasks> internTasksList = internTaskService.fetchAllInternTasks(requestedIntern, requestedTaskStatus);
        log.info("Fetched all the tasks assigned to the intern.");

        List<FetchAllTasksResponseDTO> response = new ArrayList<>();
        internTasksList.forEach(internTasks -> {
            Tasks task = internTasks.getTask();
            FetchAllTasksResponseDTO details = FetchAllTasksResponseDTO.builder()
                    .taskType(task.getTaskType())
                    .taskDescription(task.getTaskDescription())
                    .taskPriority(task.getTaskPriority())
                    .deadline(String.valueOf(internTasks.getDeadline()))
                    .build();
            response.add(details);
        });

        return response;
    }

    @Transactional
    public String submitTask(Long taskId) {
        Tasks requestedTask = taskService.findById(taskId);
        if(!requestedTask.isSubmittable()) throw new InvalidRequestException("Requested task is not submittable. You must be uploading a document. Please go through the taskDescription.");
        InternTasks requestedInternTask = internTaskService.findByInternIdAndTaskType(commonService.getExistingUser(), requestedTask);
        requestedInternTask.setTaskStatus(TaskStatus.COMPLETED);
        log.info("The requested task is marked as completed, as the intern - {} has submitted their acceptance", requestedInternTask.getIntern().getIntern().getFirstName());

        return "The task " + requestedInternTask.getTask().getTaskType() + " is marked COMPLETED by intern " + requestedInternTask.getIntern().getIntern().getFirstName();
    }

    public Resource downloadPolicyDocument(Long documentId) {
        return documentService.downloadFiles(documentId);
    }
}
