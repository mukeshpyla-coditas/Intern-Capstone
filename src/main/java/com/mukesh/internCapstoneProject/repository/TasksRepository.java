package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    boolean existsByTaskType(String taskType);
}
