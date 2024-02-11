package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("encodePassword")
    default String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    UserDto toDto(User user);

    List<UserDto> toListDto(List<User> users);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "created", expression = "java( java.time.Instant.now() )")
    User toEntity(UserAddDto dto);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    User update(@MappingTarget User user, UserAddDto dto);
}
