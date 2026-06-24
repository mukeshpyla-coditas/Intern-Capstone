package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationsRepository extends JpaRepository<Invitations, Long> {
}
