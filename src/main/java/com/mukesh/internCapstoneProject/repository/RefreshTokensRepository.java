package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.RefreshTokens;
import com.mukesh.internCapstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {
    List<RefreshTokens> findAllByIssuedTo(Users issuedTo);
}
