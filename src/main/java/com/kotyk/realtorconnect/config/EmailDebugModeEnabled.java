package com.kotyk.realtorconnect.config;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

public class EmailDebugModeEnabled extends AllNestedConditions {

    public EmailDebugModeEnabled() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @Conditional(EmailEnabled.class)
    static class EmailEnabledCondition {
    }

    @ConditionalOnProperty(name = "email.debug-mode.enabled", havingValue = "true")
    static class DebugEnabledCondition {
    }

}