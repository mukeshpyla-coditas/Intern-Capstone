package com.mukesh.internCapstoneProject.entity;

import com.mukesh.internCapstoneProject.enums.DocumentIssueStatus;
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
@Table(name = "document_issues")
public class DocumentIssues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false)
    private Documents document;

    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @CreatedDate
    private LocalDateTime issuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_issue_status", nullable = false)
    private DocumentIssueStatus documentIssueStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id", referencedColumnName = "id", nullable = false)
    private Users raisedBy;
}
