package com.kotyk.realtorconnect.config;

import com.kotyk.realtorconnect.service.email.EmailSenderService;
import com.kotyk.realtorconnect.service.email.EmailSenderServiceBean;
import com.kotyk.realtorconnect.service.email.MockEmailSenderServiceBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailConfiguration {

    private boolean enabled;
    private DebugMode debugMode;
    private Server server;

    @Getter
    @Setter
    public static class DebugMode {
        private boolean enabled;
        private String debugEmail;
    }

    @Getter
    @Setter
    public static class Server {
        private String host;
        private int port;
        private String username;
        private String password;
        private String protocol;
        private boolean testConnection;
        private boolean smtpAuth;
        private boolean smtpStartTlsEnabled;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(this.server.host);
        mailSender.setPort(this.server.port);
        mailSender.setUsername(this.server.username);
        mailSender.setPassword(this.server.password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.protocol", this.server.protocol);
        props.put("mail.test-connection", this.server.testConnection);
        props.put("mail.smtp.auth", this.server.smtpAuth);
        props.put("mail.smtp.starttls.enable", this.server.smtpStartTlsEnabled);

        return mailSender;
    }

    @Bean
    public EmailSenderService emailSenderService() {
        return this.enabled ? new EmailSenderServiceBean(javaMailSender()) : new MockEmailSenderServiceBean();
    }

}