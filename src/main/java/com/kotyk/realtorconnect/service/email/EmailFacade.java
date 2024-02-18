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

    private void logEmail(Email email) {
        log.debug("Email not sent because emailing disabled. Email: {}", email);
    }


    public void sendVerifyEmail(User user, String token) {
        Email email = emailGeneratorService.generateVerifyEmail(user, token);
        if (emailSenderService.isEmpty()) {
            logEmail(email);
            return;
        }
        emailSenderService.get().sendEmail(email);
    }

    public void sendStartPremiumEmail(Realtor realtor, int durationInMonths) {
        Email email = emailGeneratorService.generateStartPremiumEmail(realtor, durationInMonths);
        if (emailSenderService.isEmpty()) {
            logEmail(email);
            return;
        }
        emailSenderService.get().sendEmail(email);
    }


    public void sendPremiumExpiresEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiresEmail(realtor);
        if (emailSenderService.isEmpty()) {
            logEmail(email);
            return;
        }
        emailSenderService.get().sendEmail(email);
    }

    public void sendPremiumExpiredEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiredEmail(realtor);
        if (emailSenderService.isEmpty()) {
            logEmail(email);
            return;
        }
        emailSenderService.get().sendEmail(email);
    }

}
