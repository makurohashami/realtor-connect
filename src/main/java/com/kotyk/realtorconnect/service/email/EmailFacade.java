package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class EmailFacade {

    private final EmailGeneratorService emailGeneratorService;
    private final Optional<EmailSenderService> emailSenderService;

    private void sendEmail(Email email) {
        if (emailSenderService.isEmpty()) {
            log.debug("Email not sent because emailing disabled. Email: {}", email);
            return;
        }
        emailSenderService.get().sendEmail(email);
    }

    public void sendVerifyEmail(User user, String token) {
        Email email = emailGeneratorService.generateVerifyEmail(user, token);
        sendEmail(email);
    }

    public void sendStartPremiumEmail(Realtor realtor, int durationInMonths) {
        Email email = emailGeneratorService.generateStartPremiumEmail(realtor, durationInMonths);
        sendEmail(email);
    }


    public void sendPremiumExpiresEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiresEmail(realtor);
        sendEmail(email);
    }

    public void sendPremiumExpiredEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiredEmail(realtor);
        sendEmail(email);
    }

}
