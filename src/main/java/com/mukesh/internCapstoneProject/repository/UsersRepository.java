package com.mukesh.internCapstoneProject.repository;

import com.mukesh.internCapstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
