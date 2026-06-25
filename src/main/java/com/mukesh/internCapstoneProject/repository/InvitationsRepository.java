package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationsRepository extends JpaRepository<Invitations, Long> {
    Optional<Invitations> findByInviteCode(String inviteCode);
}
