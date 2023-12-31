package com.kotyk.realtorconnect.dto.user;

import com.kotyk.realtorconnect.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private Role role;
    private Instant lastLogin;
    private Boolean blocked;

}
