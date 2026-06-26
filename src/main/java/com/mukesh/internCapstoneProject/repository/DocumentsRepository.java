package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Documents;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    Page<Documents> findAllByInternId(Interns internId, Pageable pageable);

    Page<Documents> findAllByInternIdAndRequireManagerApproval(Interns internId, boolean requireManagerApproval, Pageable pageable);

    boolean existsByDocumentTypeAndInternId(DocumentType documentType, Interns internId);
}
