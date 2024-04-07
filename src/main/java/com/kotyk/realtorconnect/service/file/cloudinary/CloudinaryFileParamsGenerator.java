package com.kotyk.realtorconnect.service.file.cloudinary;

import com.cloudinary.EagerTransformation;
import com.kotyk.realtorconnect.config.CloudinaryConfiguration.CloudinaryEnabled;
import com.kotyk.realtorconnect.config.FileConfiguration;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.service.file.FileParamsGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Conditional(CloudinaryEnabled.class)
public class CloudinaryFileParamsGenerator implements FileParamsGenerator {

    @Value("${network.defaultAvatarUrl}")
    private String defaultAvatarUrl;

    private final FileConfiguration fileConfiguration;

    @Override
    public Map<String, Object> generateParamsForAvatar(User user) {

        int sizeForSave = fileConfiguration.getAvatar().getWidthHeightForSave();
        EagerTransformation transformation = new EagerTransformation().height(sizeForSave).width(sizeForSave).crop("fill").gravity("auto");

        Map<String, Object> params = new HashMap<>();
        params.put("tags", "avatar");
        params.put("transformation", transformation);
        if (user.getAvatar() != null && !user.getAvatar().equals(defaultAvatarUrl)) {
            params.put("public_id", user.getAvatarId());
        } else {
            params.put("folder", "avatars");
        }

        return params;
    }

    @Override
    public Map<String, Object> generateParamsForRealEstatePhoto(RealEstate realEstate) {
        Map<String, Object> params = new HashMap<>();
        params.put("tags", "realEstatePhoto");
        params.put("folder", "realestates/" + realEstate.getId());
        return params;
    }
}
