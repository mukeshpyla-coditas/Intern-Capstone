package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.PolicyDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyDocumentsRepository extends JpaRepository<PolicyDocuments, Long> {
}
