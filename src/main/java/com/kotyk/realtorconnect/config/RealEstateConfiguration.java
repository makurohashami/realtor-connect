package com.kotyk.realtorconnect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "real-estate")
public class RealEstateConfiguration {

    private long daysForExpireCalled;
    private Photo photo;

    @Getter
    @Setter
    public static class Photo {
        private long maxPhotosCount;
    }

}
