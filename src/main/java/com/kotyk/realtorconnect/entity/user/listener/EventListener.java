package com.kotyk.realtorconnect.entity.user.listener;

import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.service.file.FileUploaderService;
import jakarta.persistence.PostRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static FileUploaderService fileUploaderService;

    @Autowired
    public void init(FileUploaderService fileUploaderService) {
        EventListener.fileUploaderService = fileUploaderService;
    }

    @PostRemove
    public void postRemove(User user) {
        if (user.getAvatarId() != null) {
            fileUploaderService.deleteFile(user.getAvatarId());
        }
    }

}
