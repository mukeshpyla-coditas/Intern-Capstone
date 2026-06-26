package com.mukesh.internCapstoneProject.tools;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.dto.response.OnboardingDataResponseDTO;
import com.mukesh.internCapstoneProject.service.InternChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InternTools {
    private final InternChatService internChatService;

    @Tool(description = "use to send the profile info of the logged in intern.")
    public FetchInternsResponseDTO getProfile() {
        return internChatService.getProfile();
    }

    @Tool(description = "This is a tool that you need to use when a user asks for their onboarding roadmap.")
    public OnboardingDataResponseDTO getOnboardingRoadMapOfCurrentIntern() {
        return OnboardingDataResponseDTO.builder()
                .response(internChatService.getRoadMapOfCurrentIntern())
                .build();
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
