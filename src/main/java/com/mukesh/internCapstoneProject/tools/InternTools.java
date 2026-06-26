package com.mukesh.internCapstoneProject.tools;

import com.mukesh.internCapstoneProject.dto.response.FetchInternsResponseDTO;
import com.mukesh.internCapstoneProject.service.InternChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternTools {
    private final InternChatService internChatService;

    @Tool(description = "use to send the profile info of logged in user.")
    public FetchInternsResponseDTO getProfile() {
        return internChatService.getProfile();
    }
}
