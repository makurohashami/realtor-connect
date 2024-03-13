package com.kotyk.realtorconnect.service.file;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class PlaceholderFileEnabledCondition extends AllNestedConditions {

    public PlaceholderFileEnabledCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(name = "files.client", havingValue = "placeholder")
    static class PlaceholderIsFilesClient {
    }


}
