package com.kotyk.realtorconnect.service.file;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Primary
@Component
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
