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
        sendEmail(emailGeneratorService.generateVerifyEmail(user, token));
    }

    public void sendStartPremiumEmail(Realtor realtor, int durationInMonths) {
        sendEmail(emailGeneratorService.generateStartPremiumEmail(realtor, durationInMonths));
    }


    public void sendPremiumExpiresEmail(Realtor realtor) {
        sendEmail(emailGeneratorService.generatePremiumExpiresEmail(realtor));
    }

    public void sendPremiumExpiredEmail(Realtor realtor) {
        sendEmail(emailGeneratorService.generatePremiumExpiredEmail(realtor));
    }

}
