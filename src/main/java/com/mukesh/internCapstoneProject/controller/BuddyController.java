package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buddy")
@RequiredArgsConstructor
@Tag(name = "AI related APIs")
public class BuddyController {
    private final ChatService chatService;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "text") String text) {
        return chatService.chat(text);
    }
}
