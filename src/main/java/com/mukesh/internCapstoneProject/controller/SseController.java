package com.mukesh.internCapstoneProject.controller;

import com.mukesh.internCapstoneProject.service.SseEmitterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
@Tag(name = "SSE related APIs")
public class SseController {
    private final SseEmitterService sseEmitterService;

    @GetMapping("/subscribe/{userId}")
    public void subscribeForEmitter(@PathVariable Long userId) {
        sseEmitterService.subscribe(userId);
    }
}
