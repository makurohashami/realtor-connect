package com.kotyk.realtorconnect.config;

import com.cloudinary.Cloudinary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfiguration {

    private String cloudinaryUrl;

    @Bean
    @Conditional(CloudinaryEnabled.class)
    public Cloudinary cloudinary() {
        return new Cloudinary(getCloudinaryUrl());
    }

    public static class CloudinaryEnabled extends AllNestedConditions {

        public CloudinaryEnabled() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(name = "cloudinary.enabled", havingValue = "true")
        static class CloudinaryEnabledProperty {
        }
    }

}
