package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
}
