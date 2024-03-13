package com.kotyk.realtorconnect.service.email;

import com.kotyk.realtorconnect.config.EmailConfiguration;
import com.kotyk.realtorconnect.config.EmailConfiguration.EmailDebugModeEnabled;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
@Conditional(EmailDebugModeEnabled.class)
public class DebugEmailFromInterceptor {

    private final EmailConfiguration emailConfiguration;

    @Around("execution(* org.springframework.mail.javamail.JavaMailSender.send(..))")
    public Object interceptSendDebugEmailTo(ProceedingJoinPoint joinPoint) throws Throwable {
        findMimeMessages(joinPoint).forEach(this::setDebugEmailFrom);
        return joinPoint.proceed(joinPoint.getArgs());
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

    private void setDebugEmailFrom(MimeMessage mimeMessage) {
        try {
            mimeMessage.setFrom(emailConfiguration.getDebugMode().getFrom());
        } catch (MessagingException ex) {
            log.error("", ex);
        }
    }

}
