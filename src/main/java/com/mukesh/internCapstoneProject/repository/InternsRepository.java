package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternsRepository extends JpaRepository<Interns, Long> {
    Optional<Interns> findByIntern(Users intern);
}
