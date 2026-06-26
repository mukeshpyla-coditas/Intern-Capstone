package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.RefreshTokens;
import com.mukesh.internCapstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {
    List<RefreshTokens> findAllByIssuedTo(Users issuedTo);

    RefreshTokens findByRefreshToken(String refreshToken);

    Optional<RefreshTokens> findByRefreshTokenAndIsRevoked(String refreshToken, boolean isRevoked);
}
