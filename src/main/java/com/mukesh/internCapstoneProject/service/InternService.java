package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.FetchAllTasksResponseDTO;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Invitations;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.OnboardingStatus;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void saveIntern(Users intern, Invitations invitation) {
        Interns newIntern = Interns.builder()
                .intern(intern)
                .managerId(invitation.getManager())
                .onboardedHrId(commonService.getExistingUser())
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

    public List<FetchAllTasksResponseDTO> fetchAllTasks(Long internId) {
        Interns requestedIntern = internsRepository.findById(internId).orElseThrow(() -> new NotFoundException("Intern with the specified ID is not found."));
        List<InternTasks> internTasksList = internTaskService.fetchAllInternTasks(requestedIntern);
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
}
