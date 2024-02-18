package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.dto.email.Email;
import com.kotyk.realtorconnect.dto.email.EmailTemplate;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailGeneratorService {

    @Value("${network.verify-email-url}")
    private String verifyEmailUrl;

    private final SpringTemplateEngine templateEngine;

    protected Email generateVerifyEmail(User user, String token) {
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getName());
        templateVariables.put("link", verifyEmailUrl + token);

        return generateEmail(user.getEmail(), EmailTemplate.VERIFY_EMAIL, templateVariables);
    }

    protected Email generateStartPremiumEmail(Realtor realtor, int durationInMonths) {
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", realtor.getName());
        templateVariables.put("durationInMonths", durationInMonths);
        templateVariables.put("expiresAt", realtor.getPremiumExpiresAt());

        return generateEmail(realtor.getEmail(), EmailTemplate.PREMIUM_ADDED, templateVariables);
    }

    protected Email generatePremiumExpiresEmail(Realtor realtor) {
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", realtor.getName());
        templateVariables.put("daysLeft", ChronoUnit.DAYS.between(Instant.now(), realtor.getPremiumExpiresAt()) + 1);
        templateVariables.put("expiresAt", realtor.getPremiumExpiresAt());

        return generateEmail(realtor.getEmail(), EmailTemplate.PREMIUM_EXPIRES, templateVariables);
    }

    protected Email generatePremiumExpiredEmail(Realtor realtor) {
        return generateEmail(realtor.getEmail(), EmailTemplate.PREMIUM_EXPIRED, Map.of("name", realtor.getName()));
    }

    private Email generateEmail(String to, EmailTemplate template, Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        String body = templateEngine.process(template.getTemplatePath(), context);

        return Email.builder()
                .to(to)
                .body(body)
                .isHtml(template.isHtml())
                .subject(template.getSubject())
                .build();
    }
}
