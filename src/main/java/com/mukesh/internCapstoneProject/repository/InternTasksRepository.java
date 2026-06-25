package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.InternTasks;
import com.mukesh.internCapstoneProject.entity.Interns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternTasksRepository extends JpaRepository<InternTasks, Long> {
    Optional<List<InternTasks>> findAllByIntern(Interns intern);
}
