package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.response.NotificationDTO;
import com.mukesh.internCapstoneProject.exception.NotificationServiceException;
import com.mukesh.internCapstoneProject.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterService {
    private final UsersRepository usersRepository;
    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public void subscribe(Long userId) {
        if(usersRepository.existsById(userId)) {
            SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
            sseEmitterMap.put(userId, emitter);
            emitter.onCompletion(() -> sseEmitterMap.remove(userId));
            emitter.onError(e -> sseEmitterMap.remove(userId));
            emitter.onTimeout(() -> sseEmitterMap.remove(userId));
            emitter.complete();
            log.info("User with ID {} is successfully subscribed", userId);
        }
    }

    public void sendNotification(String title, String message, Long userId) {
        NotificationDTO notification = NotificationDTO.builder()
                .title(title)
                .message(message)
                .build();
        if(sseEmitterMap.containsKey(userId)) {
            SseEmitter emitter = sseEmitterMap.get(userId);
            try {
                emitter.send(SseEmitter.event().name("Notification").data(notification, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                throw new NotificationServiceException("There is an exception while sending the notification to the subscriber.");
            }
            emitter.complete();
            log.info("Notification has been sent to the browser.");
        }
    }
}
