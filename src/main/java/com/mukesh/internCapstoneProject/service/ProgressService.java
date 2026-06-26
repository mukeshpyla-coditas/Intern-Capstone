package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.ProgressResponseDTO;
import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.Roles;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternTasksRepository;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressService {
    private final InternTasksRepository internTasksRepository;
    private final InternsRepository internsRepository;
    private final CommonServiceImpl commonServiceImpl;

    public ProgressResponseDTO getProgressOfIntern(Long internId) {
        Interns requestedIntern = internsRepository.findById(internId).orElseThrow(() -> new NotFoundException("Intern with specified ID is not found."));
        Users currentUser = commonServiceImpl.getExistingUser();
        if(currentUser.getRole().equals(Roles.MANAGER)) {
            if(!requestedIntern.getManagerId().equals(currentUser)) throw new InvalidRequestException("You cannot access the records of team-members of other departments.");
            return calculateProgress(requestedIntern);
        }
        else if(currentUser.getRole().equals(Roles.NEW_HIRE)) {
            if(!currentUser.equals(requestedIntern.getIntern())) throw new InvalidRequestException("You as an INTERN, can only access the progress of yourself. Please re-verify the entered INTERN-ID and try again.");
            return calculateProgress(requestedIntern);
        }

        return calculateProgress(requestedIntern);
    }

    public ProgressResponseDTO calculateProgress(Interns intern) {
        List<InternTasks> listOfAssignedTasks = internTasksRepository.findAllByIntern(intern).orElseThrow(() -> new NotFoundException("No tasks were found mapped with the requested intern."));
        int totalAssignedTasksCount = listOfAssignedTasks.size();
        int countOfCompletedTasks = 0;
        double progressPercentage = 0D;
        for(InternTasks internTask : listOfAssignedTasks) {
            if(internTask.getTaskStatus().equals(TaskStatus.COMPLETED)) countOfCompletedTasks++;
        }
        if(countOfCompletedTasks != 0) progressPercentage = ((double) totalAssignedTasksCount / countOfCompletedTasks) * 100.0;

        return ProgressResponseDTO.builder()
                .internId(intern.getId())
                .progress(progressPercentage)
                .internName(intern.getIntern().getFirstName() + " " + intern.getIntern().getLastName())
                .message("Out of " + totalAssignedTasksCount + " tasks " + countOfCompletedTasks + " tasks are completed by the candidate.")
                .build();
    }
}
