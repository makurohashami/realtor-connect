package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

}
