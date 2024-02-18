package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.entity.user.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Value("${network.defaultAvatarUrl}")
    private String avatarUrl;

    @Named("getDefaultAvatarUrl")
    public String getDefaultAvatarUrl() {
        return avatarUrl;
    }

    @Named("encodePassword")
    public String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    public abstract UserDto toDto(User user);

    public abstract UserFullDto toFullDto(User user);

    @IterableMapping(elementTargetType = UserFullDto.class)
    public abstract List<UserFullDto> toListFullDto(List<User> users);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "avatar", expression = "java( this.getDefaultAvatarUrl() )")
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "created", expression = "java( java.time.Instant.now() )")
    public abstract User toEntity(UserAddDto dto);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    public abstract User update(@MappingTarget User user, UserAddDto dto);
}
