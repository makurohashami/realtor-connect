package com.kotyk.realtorconnect.entity.realestate.listener;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.service.file.FileUploaderService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstateListener {

    private static FileUploaderService fileUploaderService;

    @Autowired
    public void init(FileUploaderService fileUploaderService) {
        RealEstateListener.fileUploaderService = fileUploaderService;
    }

    @PostRemove
    public void postRemove(RealEstate realEstate) {
        fileUploaderService.deleteFolder("realestates/" + realEstate.getId());
    }

}
