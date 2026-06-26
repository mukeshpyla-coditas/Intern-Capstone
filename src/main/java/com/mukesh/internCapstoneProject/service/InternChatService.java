package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.entity.Interns;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.exception.NotFoundException;
import com.mukesh.internCapstoneProject.repository.InternsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternChatService {
    private final CommonServiceImpl commonService;
    private final InternsRepository internsRepository;

    public FetchInternsResponseDTO getProfile() throws NotFoundException{
        Users currentUser = commonService.getExistingUser();
        try {
            Interns currentIntern = internsRepository.findByIntern(currentUser).get();
            return FetchInternsResponseDTO.builder()
                    .internId(currentIntern.getId())
                    .internEmail(currentUser.getEmail())
                    .internName(currentUser.getFirstName() + " " + currentUser.getLastName())
                    .onboardedHrName(currentIntern.getOnboardedHrId().getFirstName() + " " + currentIntern.getOnboardedHrId().getLastName())
                    .onboardingStatus(currentIntern.getOnboardingStatus().name())
                    .build();
        } catch (Exception exception) {
            throw new NotFoundException("No intern with specified credentials found");
        }
    }
}
