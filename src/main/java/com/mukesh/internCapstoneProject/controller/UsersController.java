package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserLoginRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserRegisterRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users related APIs")
public class UsersController {
    private final UserServiceImpl usersService;

    // Use just to populate the HR data
    @PostMapping("/register")
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
                "User log-in successful",
                response
        );
    }

    // Invitation sent to the manager
    @PostMapping("/invite/manager")
    public ResponseEntity<ApiResponse<String>> inviteManager(@RequestBody @Valid InviteUserRequestDTO request) {
        String response = usersService.inviteManager(request);
        return ApiResponse.success(
                HttpStatus.OK,
                "Invite to the specified Manager-mailID is sent.",
                response
        );
    }

    // Invitation sent to the intern
    @PostMapping("/invite/intern/{managerId}")
    public ResponseEntity<ApiResponse<String>> inviteIntern(@PathVariable Long managerId, @RequestBody @Valid InviteUserRequestDTO request) {
        String response = usersService.inviteIntern(request, managerId);
        return ApiResponse.success(
                HttpStatus.OK,
                "Invite to the specified intern-mailID is sent.",
                response
        );
    }

    // Registering the Manager
    @PostMapping("/register/manager/{inviteCode}")
    public ResponseEntity<ApiResponse<String>> registerManager(@PathVariable String inviteCode, @RequestBody @Valid UserRegisterRequestDTO request) {
        String response = usersService.registerManager(request, inviteCode);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "Manager registered successfully",
                response
        );
    }

    // Registering the Intern
    @PostMapping("/register/intern/{inviteCode}")
    public ResponseEntity<ApiResponse<String>> registerIntern(@PathVariable String inviteCode, @RequestBody @Valid UserRegisterRequestDTO request) {
        String response = usersService.registerIntern(request, inviteCode);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "Intern registered successfully",
                response
        );
    }
}
