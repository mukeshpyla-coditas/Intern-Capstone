package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;
import com.mukesh.internCapstoneProject.entity.RefreshTokens;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.exception.InvalidRequestException;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.RefreshTokensRepository;
import com.mukesh.internCapstoneProject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokensRepository refreshTokensRepository;

    @Transactional
    public UserLoginResponseDTO regenerateAccessToken(String refreshToken) {
        RefreshTokens currentRefreshToken = refreshTokensRepository.findByRefreshTokenAndIsRevoked(refreshToken, false)
                .orElseThrow(() -> new NotFoundException("RefreshToken provided is revoked or not-valid. So, please re-verify the request."));
        if(currentRefreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            currentRefreshToken.setRevoked(true);
            throw new InvalidRequestException("Your refreshToken has been expired. So, please re-login into the application.");
        }
        Users requestedUser = currentRefreshToken.getIssuedTo();
        String accessToken = jwtUtil.generateAccessToken(requestedUser.getUsername(), requestedUser.getRole().name());
        return UserLoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userRole(requestedUser.getRole().name())
                .message("Please NOTE that the accessToken will be active for the next 10min.")
                .build();
    }
}
