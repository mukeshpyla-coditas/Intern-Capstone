package com.mukesh.internCapstoneProject.service.implementations;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserLoginRequestDTO;
import com.mukesh.internCapstoneProject.dto.request.UserRegisterRequestDTO;
import com.mukesh.internCapstoneProject.dto.response.UserLoginResponseDTO;
import com.mukesh.internCapstoneProject.entity.RefreshTokens;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.Roles;
import com.mukesh.internCapstoneProject.exception.AuthenticationException;
import com.mukesh.internCapstoneProject.exception.EntityAlreadyExistsException;
import com.mukesh.internCapstoneProject.exception.ExceptionMessages;
import com.mukesh.internCapstoneProject.repository.RefreshTokensRepository;
import com.mukesh.internCapstoneProject.repository.UsersRepository;
import com.mukesh.internCapstoneProject.service.interfaces.MailService;
import com.mukesh.internCapstoneProject.service.interfaces.UsersService;
import com.mukesh.internCapstoneProject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CommonServiceImpl commonService;
    private final RefreshTokensRepository refreshTokensRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public String registerUser(UserRegisterRequestDTO request) {
        if(usersRepository.existsByUsername((request.username()))) throw new EntityAlreadyExistsException(ExceptionMessages.USER_WITH_USERNAME_EXISTS);
        if(usersRepository.existsByEmail(request.email())) throw new EntityAlreadyExistsException(ExceptionMessages.USER_WITH_EMAIL_EXISTS);

        Users newUser = Users.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Roles.HR)
                .contactNumber(request.contactNumber())
                .email(request.email())
                .isActive(true)
                .build();
        usersRepository.save(newUser);
        log.info("HR {} is successfully registered", newUser.getFirstName());

        return "HR " + newUser.getFirstName() + " is successfully registered.";
    }

    @Override
    @Transactional
    public UserLoginResponseDTO loginUser(UserLoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            Users requestedUser = usersRepository.findByUsername(request.username()).orElseThrow();
            List<RefreshTokens> refreshTokensList = refreshTokensRepository.findAllByIssuedTo(requestedUser);
            if(!Objects.isNull(refreshTokensList)) refreshTokensList.forEach(refreshTokens -> refreshTokens.setRevoked(true));

            String accessToken = jwtUtil.generateAccessToken(requestedUser.getUsername(), requestedUser.getRole().name());
            String refreshToken = UUID.randomUUID().toString();
            RefreshTokens refreshTokens = RefreshTokens.builder()
                    .refreshToken(refreshToken)
                    .issuedTo(requestedUser)
                    .issuedAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(1))
                    .isRevoked(false)
                    .build();
            refreshTokensRepository.save(refreshTokens);
            log.info("Access Token and a new Refresh Token is created for the user {}.", requestedUser.getFirstName());

            return UserLoginResponseDTO.builder()
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .userRole(requestedUser.getRole().name())
                    .message(ExceptionMessages.USER_LOGIN_MESSAGE)
                    .build();
        } catch(Exception exception) {
            throw new AuthenticationException(ExceptionMessages.USER_UNAUTHORIZED_EXCEPTION);
        }
    }

    @Override
    public String inviteManager(InviteUserRequestDTO request) {
        return mailService.sendInvitation(request, "/api/v1/register", commonService.getExistingUser(), Roles.MANAGER);
    }

    @Override
    public String inviteIntern(InviteUserRequestDTO request) {
        return mailService.sendInvitation(request, "/api/v1/register", commonService.getExistingUser(), Roles.NEW_HIRE);
    }
}
