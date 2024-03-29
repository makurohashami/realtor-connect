package com.kotyk.realtorconnect.config;

import jakarta.servlet.MultipartConfigElement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "files")
public class FileConfiguration {

    private String client;
    private List<String> allowedContentTypes;
    private AvatarConfig avatar;
    private RealEstatePhotoConfig realEstatePhoto;
    private MultipartConfig multipart;

    @Getter
    @Setter
    public static class AvatarConfig {
        private int minWidthHeight;
        private int maxWidthHeight;
        private int widthHeightForSave;
    }

    @Getter
    @Setter
    public static class RealEstatePhotoConfig {
        private int minWidthHeight;
        private int maxWidthHeight;
    }

    @Getter
    @Setter
    public static class MultipartConfig {
        private String maxFileSize;
        private String maxRequestSize;
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(getMultipart().getMaxFileSize()));
        factory.setMaxRequestSize(DataSize.parse(getMultipart().getMaxRequestSize()));
        return factory.createMultipartConfig();
    }

}
