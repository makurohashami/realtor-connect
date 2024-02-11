package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailFacade {

    private final EmailSenderService emailSenderService;
    private final EmailGeneratorService emailGeneratorService;


    public void sendVerifyEmail(User user, String token) {
        Email email = emailGeneratorService.generateVerifyEmail(user, token);
        emailSenderService.sendEmail(email);
    }

    public void sendStartPremiumEmail(Realtor realtor, int durationInMonths) {
        Email email = emailGeneratorService.generateStartPremiumEmail(realtor, durationInMonths);
        emailSenderService.sendEmail(email);
    }


    public void sendPremiumExpiresEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiresEmail(realtor);
        emailSenderService.sendEmail(email);
    }

    public void sendPremiumExpiredEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiredEmail(realtor);
        emailSenderService.sendEmail(email);
    }

}
