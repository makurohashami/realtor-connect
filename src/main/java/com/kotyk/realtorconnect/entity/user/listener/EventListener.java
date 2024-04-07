package com.kotyk.realtorconnect.entity.user.listener;

import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.service.file.FileService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static FileService fileService;

    @Autowired
    public void init(FileService fileService) {
        EventListener.fileService = fileService;
    }

    @PostRemove
    public void postRemove(User user) {
        if (user.getAvatarId() != null) {
            fileService.deleteFile(user.getAvatarId());
        }
    }

}
