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


    public void sendVerifyEmail(User user) {
        Email email = emailGeneratorService.generateVerifyEmail(user);
        emailSenderService.sendEmail(email);
    }

    public void sendStartPremiumNotification(Realtor realtor, short durationInMonths) {
        Email email = emailGeneratorService.generateStartPremiumNotification(realtor, durationInMonths);
        emailSenderService.sendEmail(email);
    }


    public void sendSubscriptionExpiresEmail(Realtor realtor) {
        Email email = emailGeneratorService.generateSubscriptionExpiresEmail(realtor);
        emailSenderService.sendEmail(email);
    }

    public void sendSubscriptionExpiredEmail(Realtor realtor) {
        Email email = emailGeneratorService.generatePremiumExpiredEmail(realtor);
        emailSenderService.sendEmail(email);
    }

}
