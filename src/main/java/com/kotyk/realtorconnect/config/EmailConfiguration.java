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
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailConfiguration {

    private boolean enabled;
    private Server server;
    private DebugMode debugMode;

    @Getter
    @Setter
    public static class Server {

        private Credentials credentials;
        private Network network;
        private Map<String, String> properties;

        @Getter
        @Setter
        public static class Credentials {
            private String username;
            private String password;
        }

        @Getter
        @Setter
        public static class Network {
            private String host;
            private Integer port;
        }

    }

    @Getter
    @Setter
    public static class DebugMode {
        private boolean enabled;
        private String from;
        private Server debugServer;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        if (this.getDebugMode().isEnabled()) {
            return configureJavaMailSender(this.getDebugMode().getDebugServer());
        }
        return configureJavaMailSender(this.getServer());
    }

    private JavaMailSender configureJavaMailSender(Server server) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(server.getNetwork().getHost());
        mailSender.setPort(server.getNetwork().getPort());
        mailSender.setUsername(server.getCredentials().getUsername());
        mailSender.setPassword(server.getCredentials().getPassword());

        if (!CollectionUtils.isEmpty(server.getProperties())) {
            Properties props = mailSender.getJavaMailProperties();
            props.putAll(server.getProperties());
        }

        return mailSender;
    }

    @Bean
    public EmailSenderService emailSenderService() {
        return this.enabled ? new EmailSenderServiceBean(javaMailSender()) : new MockEmailSenderServiceBean();
    }

}
