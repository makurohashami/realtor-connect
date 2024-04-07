package com.kotyk.realtorconnect.entity.realestate.listener;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.service.file.FileService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealEstateListener {

    private static FileService fileService;

    @Autowired
    public void init(FileService fileService) {
        RealEstateListener.fileService = fileService;
    }

    @PostRemove
    public void postRemove(RealEstate realEstate) {
        fileService.deleteFolder("realestates/" + realEstate.getId());
    }

}
