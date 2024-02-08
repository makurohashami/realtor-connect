package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockEmailSenderServiceBean implements EmailSenderService {
    @Override
    public void sendEmail(Email email) {
        log.debug("Email not sent because emailing disabled. Email: {}", email);
    }
}
