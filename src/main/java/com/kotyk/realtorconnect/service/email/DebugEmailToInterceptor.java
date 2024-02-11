package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.config.EmailConfiguration;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class DebugEmailToInterceptor {

    private final EmailConfiguration emailConfiguration;

    @Around("execution(* org.springframework.mail.javamail.JavaMailSender.send(..))")
    public Object interceptSendDebugEmailTo(ProceedingJoinPoint joinPoint) throws Throwable {
        if (isDebugModeEnabled()) {
            findMimeMessages(joinPoint).forEach(this::setDebugEmailTo);
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private boolean isDebugModeEnabled() {
        return emailConfiguration.getDebugMode().isEnabled();
    }

    private List<MimeMessage> findMimeMessages(ProceedingJoinPoint joinPoint) {
        List<MimeMessage> retList = new LinkedList<>();
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
            if (arg instanceof MimeMessage) {
                retList.add((MimeMessage) arg);
            } else if (arg instanceof MimeMessage[]) {
                retList.addAll(Arrays.asList((MimeMessage[]) arg));
            }
        });
        return retList;
    }

    private void setDebugEmailTo(MimeMessage mimeMessage) {
        try {
            Address debugAddress = new InternetAddress(emailConfiguration.getDebugMode().getDebugEmail());
            mimeMessage.setRecipient(Message.RecipientType.TO, debugAddress);
        } catch (MessagingException ex) {
            log.error("", ex);
        }
    }

}
