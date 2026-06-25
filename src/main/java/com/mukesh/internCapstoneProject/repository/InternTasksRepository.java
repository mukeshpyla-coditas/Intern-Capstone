package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Tasks;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternTasksRepository extends JpaRepository<InternTasks, Long> {
    Optional<List<InternTasks>> findAllByInternAndTaskStatus(Interns intern, TaskStatus taskStatus);

    Optional<InternTasks> findByInternAndTask(Interns intern, Tasks task);

    Optional<List<InternTasks>> findAllByIntern(Interns intern);

    @Query("select count(i) from InternTasks i")
    long countFirstBy();
}
