package com.mukesh.internCapstoneProject.service;

import com.mukesh.internCapstoneProject.dto.request.InviteUserRequestDTO;
import com.mukesh.internCapstoneProject.entity.Invitations;
import com.mukesh.internCapstoneProject.entity.Users;
import com.mukesh.internCapstoneProject.enums.InvitationStatus;
import com.mukesh.internCapstoneProject.enums.Roles;
import com.mukesh.internCapstoneProject.exception.MailSenderException;
import com.mukesh.internCapstoneProject.repository.InvitationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final InvitationsRepository invitationsRepository;

    public void sendMail(String senderMail, String receiverMail, String subject, String message) throws MailSenderException{
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderMail);
            simpleMailMessage.setTo(receiverMail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);

            javaMailSender.send(simpleMailMessage);
            log.info("Mail is successfully sent");
        } catch (Exception exception) {
            throw new MailSenderException("There is an error while sending from " + senderMail + " to " + receiverMail);
        }
    }

    public String sendInvitation(InviteUserRequestDTO request, String url, Users sender, Roles role) {
        String inviteToken = UUID.randomUUID().toString();
        String link = "https://playtime-sanitary-nutcase.ngrok-free.dev" + url + inviteToken;
        String mailMessage = request.mailMessage() + "\n" + link;

        Invitations newInvitation = Invitations.builder()
                .inviteCode(inviteToken)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .receiverMail(request.receiverMail())
                .invitationStatus(InvitationStatus.ISSUED)
                .role(role)
                .build();
        invitationsRepository.save(newInvitation);
        log.info("Created a new new invitation for the requested user: {}", request.receiverMail());

        try {
            sendMail(sender.getEmail(), request.receiverMail(), request.mailSubject(), mailMessage);
            log.error("There was an error while sending the mail.");
        } catch (MailSenderException exception) {
            throw new MailSenderException("There was an exception while sending the mail");
        }

        return "Mail is successfully sent";
    }
}
