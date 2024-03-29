package com.kotyk.realtorconnect.service.file;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;

import java.util.Map;

public interface FileParamsGenerator {

    Map<String, Object> generateParamsForAvatar(User user);

    Map<String, Object> generateParamsForRealEstatePhoto(RealEstate realEstate);

}
