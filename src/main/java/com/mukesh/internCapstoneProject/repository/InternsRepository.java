package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Interns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternsRepository extends JpaRepository<Interns, Long> {
}
