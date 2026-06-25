package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.entity.Invitations;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.DocumentType;
import com.mukesh.internCapstoneProject.enums.InvitationStatus;
import com.mukesh.internCapstoneProject.enums.TaskStatus;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.exception.TokenExpiredException;
import com.mukesh.internCapstoneProject.repository.InvitationsRepository;
import com.mukesh.internCapstoneProject.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl {
    private final UsersRepository usersRepository;
    private final InvitationsRepository invitationsRepository;

    public Users getExistingUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with specified username is nto found."));
    }

    public Invitations checkInviteValidity(String inviteToken) {
        Invitations existingInvitation = invitationsRepository.findByInviteCode(inviteToken).orElseThrow(() -> new NotFoundException("Invitation with specified invite is not found."));
        if(existingInvitation.getExpiresAt().isBefore(LocalDateTime.now())) throw new TokenExpiredException("Invite Token expired exception.");
        if(existingInvitation.getInvitationStatus().equals(InvitationStatus.ACCEPTED)) throw new InvalidRequestException("The invite is already ACCEPTED. Please re-verify the inviteCode and try again.");

        return existingInvitation;
    }

    public DocumentType checkDocumentType(String documentType) {
        boolean flag = false;
        DocumentType response = null;
        for(DocumentType type : DocumentType.values()) {
            if(type.name().equalsIgnoreCase(documentType)) {
                response = type;
                flag = true;
                break;
            }
        }
        if(!flag) throw new InvalidRequestException("Enter valid DocumentType");
        return response;
    }

    public TaskStatus checkTaskStatus(String taskStatus) {
        boolean flag = false;
        TaskStatus response = null;
        for(TaskStatus type : TaskStatus.values()) {
            if(type.name().equalsIgnoreCase(taskStatus)) {
                response = type;
                flag = true;
                break;
            }
        }
        if(!flag) throw new InvalidRequestException("Enter valid TaskStatus.");
        return response;
    }


}
