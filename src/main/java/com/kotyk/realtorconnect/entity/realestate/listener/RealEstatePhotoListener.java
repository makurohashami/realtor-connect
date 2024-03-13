package com.kotyk.realtorconnect.entity.realestate.listener;

import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.service.file.FileUploaderService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstatePhotoListener {

    private static FileUploaderService fileUploaderService;

    @Autowired
    public void init(FileUploaderService fileUploaderService) {
        RealEstatePhotoListener.fileUploaderService = fileUploaderService;
    }

    @PostRemove
    public void postRemove(RealEstatePhoto photo) {
        if (photo.getPhotoId() != null) {
            fileUploaderService.deleteFile(photo.getPhotoId());
        }
    }

}
