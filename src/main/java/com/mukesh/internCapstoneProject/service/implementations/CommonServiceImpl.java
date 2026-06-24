package com.mukesh.internCapstoneProject.service.implementations;

import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.UsersRepository;
import com.mukesh.internCapstoneProject.service.interfaces.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    private final UsersRepository usersRepository;

    @Override
    public Users getExistingUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with specified username is nto found."));
    }
}
