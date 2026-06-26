package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.dto.response.RoadmapResponseDTO;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternTasksRepository;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import com.mukesh.internCapstoneProject.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternChatService {
    private final CommonServiceImpl commonService;
    private final InternsRepository internsRepository;
    private final InternTasksRepository internTasksRepository;
    private final TasksRepository tasksRepository;

    public FetchInternsResponseDTO getProfile() {
        Users currentUser = commonService.getExistingUser();
        Interns currentIntern = getIntern();

        return FetchInternsResponseDTO.builder()
                .internId(currentIntern.getId())
                .internEmail(currentUser.getEmail())
                .internName(currentUser.getFirstName() + " " + currentUser.getLastName())
                .onboardedHrName(currentIntern.getOnboardedHrId().getFirstName() + " " + currentIntern.getOnboardedHrId().getLastName())
                .onboardingStatus(currentIntern.getOnboardingStatus().name())
                .build();
    }

    public List<RoadmapResponseDTO> getRoadMapOfCurrentIntern() {
        Interns currentIntern = getIntern();
        List<InternTasks> roadMapOfCurrentIntern = internTasksRepository.findAllByIntern(currentIntern).orElseThrow(() -> new NotFoundException("No tasks found for requested Intern."));
        List<RoadmapResponseDTO> roadmapResponse = new ArrayList<>();
        for(InternTasks task : roadMapOfCurrentIntern) {
            RoadmapResponseDTO details = RoadmapResponseDTO.builder()
                    .taskName(task.getTask().getTaskType())
                    .taskPriority(task.getTask().getTaskPriority())
                    .taskDescription(task.getTask().getTaskDescription())
                    .taskDeadline(String.valueOf(task.getDeadline()))
                    .build();
            roadmapResponse.add(details);
        }
        log.info("This is from the roadmap response service.");

        return roadmapResponse;
    }

    public String fetchTaskDescriptionAsPerId(Long taskId) {
        Tasks requestedTask = tasksRepository.findById(taskId).orElseThrow(() -> new NotFoundException("There is no task found with specified task"));
        return requestedTask.getTaskDescription();
    }

    public Interns getIntern() {
        Users currentUser = commonService.getExistingUser();
        return internsRepository.findByIntern(currentUser).orElseThrow(() -> new NotFoundException("Intern with specified usee credentials is not found."));
    }
}
