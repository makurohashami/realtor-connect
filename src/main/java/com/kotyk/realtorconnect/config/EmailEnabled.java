package com.kotyk.realtorconnect.config;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class EmailEnabled extends AllNestedConditions {

    public EmailEnabled() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(name = "email.enabled", havingValue = "true")
    static class EmailEnabledProperty {
    }
}
