package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {
    private final InternService internService;
    private final CommonServiceImpl commonService;

    public List<FetchInternsResponseDTO> fetchAllInterns(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Interns> internsList = internService.fetchAllInternsByManager(commonService.getExistingUser(), pageable);
        List<FetchInternsResponseDTO> response = new ArrayList<>();
        for(Interns intern : internsList.getContent()) {
            Users user = intern.getIntern();
            FetchInternsResponseDTO details = FetchInternsResponseDTO.builder()
                    .internId(intern.getId())
                    .internEmail(user.getEmail())
                    .internName(user.getFirstName() + " " + user.getLastName())
                    .onboardedHrName(intern.getOnboardedHrId().getFirstName() + " " + intern.getOnboardedHrId().getLastName())
                    .onboardingStatus(intern.getOnboardingStatus().name())
                    .build();
            response.add(details);
        }
        return response;
    }
}
