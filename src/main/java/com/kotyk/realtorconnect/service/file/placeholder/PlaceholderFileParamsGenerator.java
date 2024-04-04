package com.kotyk.realtorconnect.service.file.placeholder;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.service.file.FileParamsGenerator;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Primary
@Component
@Profile("test")
@Conditional(PlaceholderFileEnabledCondition.class)
public class PlaceholderFileParamsGenerator implements FileParamsGenerator {

    @Override
    public Map<String, Object> generateParamsForAvatar(User user) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> generateParamsForRealEstatePhoto(RealEstate realEstate) {
        return new HashMap<>();
    }

}
