package com.mukesh.internCapstoneProject.entity;

import com.mukesh.internCapstoneProject.enums.DocumentType;
import com.mukesh.internCapstoneProject.enums.HrApprovalStatus;
import com.mukesh.internCapstoneProject.enums.ManagerApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "documents")
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intern_id", referencedColumnName = "id", nullable = false)
    private Interns internId;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "document_type", nullable = false)
    private DocumentType documentType;

    @CreatedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "require_manager_approval", nullable = false)
    private ManagerApprovalStatus requireManagerApproval;

    @Enumerated(EnumType.STRING)
    @Column(name = "hr_approval_status", nullable = false)
    private HrApprovalStatus hrApprovalStatus;

    @Column(name = "document_location", nullable = false)
    private String documentLocation;
}
