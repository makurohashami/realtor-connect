package com.kotyk.realtorconnect.service.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kotyk.realtorconnect.config.CloudinaryConfiguration.CloudinaryEnabled;
import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Conditional(CloudinaryEnabled.class)
public class CloudinaryFileUploaderService implements FileUploaderService {

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

    @Override
    public void deleteFile(String path) {
        try {
            var params = ObjectUtils.asMap("async", "true");
            cloudinary.api().deleteResources(Collections.singletonList(path), params);
        } catch (Exception ex) {
            log.error("Error while deleting file", ex);
        }
    }

    @Override
    public void deleteFolder(String folder) {
        try {
            cloudinary.api().deleteResourcesByPrefix(folder, ObjectUtils.emptyMap());
            cloudinary.api().deleteFolder(folder, ObjectUtils.emptyMap());
        } catch (Exception ex) {
            log.error("Error while deleting folder", ex);
        }
    }

}
