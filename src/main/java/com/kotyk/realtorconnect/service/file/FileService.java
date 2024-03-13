package com.kotyk.realtorconnect.service.file;

import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {

    FileUploadResponse uploadFile(MultipartFile file, Map params);

    void deleteFile(String path);

    void deleteFolder(String folder);

}
