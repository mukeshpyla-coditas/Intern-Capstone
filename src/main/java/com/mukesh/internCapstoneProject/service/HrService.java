package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.request.CreateTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UpdateExistingTaskRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.CreateTaskResponseDTO;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.exception.EntityAlreadyExistsException;
import com.mukesh.internCapstoneProject.exception.ExceptionMessages;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class HrService {
    private final TasksRepository tasksRepository;

    public CreateTaskResponseDTO createTask(CreateTaskRequestDTO request) {
        if(tasksRepository.existsByTaskType(request.taskType())) throw new EntityAlreadyExistsException(ExceptionMessages.TASK_ALREADY_EXISTS);

        Tasks newTask = Tasks.builder()
                .taskPriority(request.taskPriority())
                .taskType(request.taskType())
                .taskDescription(request.taskDescription())
                .createdAt(LocalDateTime.now())
                .build();
        tasksRepository.save(newTask);
        log.info("A new task of type {} is successfully created.", newTask.getTaskType());

        return CreateTaskResponseDTO.builder()
                .taskName(newTask.getTaskType())
                .taskPriority(newTask.getTaskPriority())
                .taskDescription(newTask.getTaskDescription())
                .createdAt(String.valueOf(newTask.getCreatedAt()))
                .build();
    }

    public CreateTaskResponseDTO updateExistingTask(UpdateExistingTaskRequestDTO request) {
        Tasks requestedTask = tasksRepository.findById(request.id()).orElseThrow(() -> new NotFoundException(ExceptionMessages.TASK_NOT_FOUND_EXCEPTION));
        requestedTask.setTaskType(request.taskType());
        requestedTask.setTaskPriority(request.taskPriority());
        requestedTask.setTaskDescription(requestedTask.getTaskDescription());
        tasksRepository.save(requestedTask);
        log.info("Updated the requested task. Task ID {}", requestedTask.getId());

        return CreateTaskResponseDTO.builder()
                .taskName(requestedTask.getTaskType())
                .taskDescription(requestedTask.getTaskDescription())
                .taskPriority(requestedTask.getTaskPriority())
                .build();
    }
}
