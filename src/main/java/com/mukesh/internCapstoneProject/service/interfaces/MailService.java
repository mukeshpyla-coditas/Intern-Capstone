package com.mukesh.internCapstoneProject.service.interfaces;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.Roles;

public interface MailService {
    String sendInvitation(InviteUserRequestDTO request, String url, Users sender, Roles role);
}
