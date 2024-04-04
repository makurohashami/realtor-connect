package com.kotyk.realtorconnect.service.file.placeholder;

import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import com.kotyk.realtorconnect.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Primary
@Component
@Profile("test")
@Conditional(PlaceholderFileEnabledCondition.class)
public class PlaceholderFileService implements FileService {

    @Value("${network.defaultAvatarUrl}")
    private String defaultAvatarUrl;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, Map<String, Object> params) {
        return FileUploadResponse.builder().url(defaultAvatarUrl).build();
    }

    @Override
    public void deleteFile(String path) {
    }

    @Override
    public void deleteFolder(String folder) {
    }

}
