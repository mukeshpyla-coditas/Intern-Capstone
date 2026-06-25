package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternTasksRepository;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternTaskService {
    private final InternTasksRepository internTasksRepository;
    private final InternsRepository internsRepository;

    public void createInternTask(Interns intern, Tasks task) {
        InternTasks internTasks = InternTasks.builder()
                .intern(intern)
                .task(task)
                .taskStatus(TaskStatus.IN_PROGRESS)
                .deadline(LocalDateTime.now().plusDays(3))
                .completedAt(null)
                .build();
        internTasksRepository.save(internTasks);
        log.info("Task {} is mapped to intern {}", task.getTaskType(), intern.getIntern().getFirstName());
    }

    public List<InternTasks> fetchAllInternTasks(Interns intern, TaskStatus taskStatus) {
        return internTasksRepository.findAllByInternAndTaskStatus(intern, taskStatus).orElseThrow(() -> new NotFoundException("There are no active tasks allocated to requested intern. Please verify and try again."));
    }

    public InternTasks findByInternIdAndTaskType(Users user, Tasks task) {
        Interns requestedIntern = internsRepository.findByIntern(user).orElseThrow(() -> new NotFoundException("No intern found with specified credentials."));
        return internTasksRepository.findByInternAndTask(requestedIntern, task).orElseThrow(() -> new NotFoundException("No task with specified taskId is found for specified intern-ID."));
    }
}
