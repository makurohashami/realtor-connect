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
public class UserAuthDto {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private Instant lastLogin;

}
