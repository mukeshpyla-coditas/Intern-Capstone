package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.tools.InternTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final InternTools internTools;

    public ChatService(ChatClient.Builder builder, InternTools internTools) {
        this.chatClient = builder.build();
        this.internTools = internTools;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .system("""
                You are an AI assistant for interns.
                Your name is Onboarding Buddy.
                You help interns during onboarding.
                You can answer onboarding questions.
                If application data is needed,
                use the available tools.
                Never make up intern information.
                If you don't know something,
                politely say so."""
                )
                .tools(internTools)
                .user(message)
                .call()
                .content();
    }
}
