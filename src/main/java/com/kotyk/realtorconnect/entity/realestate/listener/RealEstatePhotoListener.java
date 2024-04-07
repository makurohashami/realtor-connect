package com.kotyk.realtorconnect.entity.realestate.listener;

import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.service.file.FileService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstatePhotoListener {

    private static FileService fileService;

    @Autowired
    public void init(FileService fileService) {
        RealEstatePhotoListener.fileService = fileService;
    }

    @PostRemove
    public void postRemove(RealEstatePhoto photo) {
        if (photo.getPhotoId() != null) {
            fileService.deleteFile(photo.getPhotoId());
        }
    }

}
