package com.kotyk.realtorconnect.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplate {

    VERIFY_EMAIL(
            "Welcome to Realtor Connect!",
            "html/verify_email.html",
            true
    ),
    PREMIUM_ADDED(
            "Congratulations on your new subscription",
            "html/premium_added.html",
            true
    ),
    PREMIUM_EXPIRES(
            "Your subscription will expire soon",
            "html/premium_expires.html",
            true
    ),
    PREMIUM_EXPIRED(
            "Your subscription has expired",
            "html/premium_expired.html",
            true
    ),
    RESET_PASSWORD(
            "Reset password",
            "html/reset_password.html",
            true
    );


    private final String subject;
    private final String templatePath;
    private final boolean isHtml;
}
