package com.kotyk.realtorconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", modifyOnCreate = false)
public class PersistenceConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
    }

}
