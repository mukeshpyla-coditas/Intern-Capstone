package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;
import com.mukesh.internCapstoneProject.global.ApiResponse;
import com.mukesh.internCapstoneProject.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refresh")
@Tag(name = "RefreshToken related APIs")
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/{refreshToken}")
    public ResponseEntity<ApiResponse<UserLoginResponseDTO>> regenerateAccessToken(@PathVariable @NotNull String refreshToken) {
        UserLoginResponseDTO response = refreshTokenService.regenerateAccessToken(refreshToken);
        return ApiResponse.success(
                HttpStatus.OK,
                "Regenerated the accessToken successfully.",
                response
        );
    }
}
