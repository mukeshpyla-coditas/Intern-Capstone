package com.mukesh.internCapstoneProject.service.interfaces;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserLoginRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserRegisterRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;

public interface UsersService {
    String registerUser(UserRegisterRequestDTO request);
    UserLoginResponseDTO loginUser(UserLoginRequestDTO request);
    String inviteManager(InviteUserRequestDTO request);
    String inviteIntern(InviteUserRequestDTO request);
}
