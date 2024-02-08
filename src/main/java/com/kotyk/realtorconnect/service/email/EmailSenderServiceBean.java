package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class EmailSenderServiceBean implements EmailSenderService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendEmail(Email email) {
        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
            messageHelper.setTo(email.getTo());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getBody(), email.isHtml());

            mailSender.send(messageHelper.getMimeMessage());

        } catch (MailException ex) {
            log.error("Error while sending email", ex);
        } catch (MessagingException ex) {
            log.error("MimeMessageHelper creation failed", ex);
        }
    }

}
