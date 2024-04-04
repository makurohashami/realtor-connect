package com.kotyk.realtorconnect.service.file.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.NotFound;
import com.cloudinary.utils.ObjectUtils;
import com.kotyk.realtorconnect.config.CloudinaryConfiguration.CloudinaryEnabled;
import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import com.kotyk.realtorconnect.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
@Conditional(CloudinaryEnabled.class)
public class CloudinaryFileService implements FileService {

    private final Cloudinary cloudinary;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, Map<String, Object> params) {
        try {
            var result = cloudinary.uploader().upload(file.getBytes(), params);
            return FileUploadResponse.builder()
                    .url((String) result.get("url"))
                    .fileId((String) result.get("public_id"))
                    .build();
        } catch (IOException ex) {
            log.error("Error while uploading file", ex);
            return FileUploadResponse.builder().build();
        }
    }

    @Async
    @Override
    public void deleteFile(String path) {
        try {
            var params = ObjectUtils.asMap("async", "true");
            cloudinary.api().deleteResources(Collections.singletonList(path), params);
        } catch (Exception ex) {
            log.error("Error while deleting file", ex);
        }
    }

    @Async
    @Override
    public void deleteFolder(String folder) {
        try {
            cloudinary.api().deleteResourcesByPrefix(folder, ObjectUtils.emptyMap());
            cloudinary.api().deleteFolder(folder, ObjectUtils.emptyMap());
        } catch (NotFound ignored) {

        } catch (Exception ex) {
            log.error("Error while deleting folder", ex);
        }
    }

}
