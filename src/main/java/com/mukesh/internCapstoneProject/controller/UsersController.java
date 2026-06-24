package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserLoginRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserRegisterRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.interfaces.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users related APIs")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/register/hr")
    public ResponseEntity<ApiResponse<String>> registerHr(@RequestBody @Valid UserRegisterRequestDTO request) {
        String response = usersService.registerUser(request);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "HR is created successfully",
                response
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDTO>> loginUser(@RequestBody @Valid UserLoginRequestDTO request) {
        UserLoginResponseDTO response = usersService.loginUser(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "User is logged-in successfully.",
                response
        );
    }

    @PostMapping("/invite/manager")
    public ResponseEntity<ApiResponse<String>> inviteManager(@RequestBody @Valid InviteUserRequestDTO request) {
        String response = usersService.inviteManager(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "Invite to the specified Manager-mail is sent.",
                response
        );
    }

    @PostMapping("/invite/intern")
    public ResponseEntity<ApiResponse<String>> inviteIntern(@RequestBody @Valid InviteUserRequestDTO request) {
        String response = usersService.inviteIntern(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "Invite to the specified intern-mail is sent.",
                response
        );
    }
}
