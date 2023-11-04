package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.user.UserAuthDto;
import com.kotyk.realtorconnect.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserAuthDto toAuthDto(User user);
}
