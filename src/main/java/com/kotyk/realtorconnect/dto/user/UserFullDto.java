package com.kotyk.realtorconnect.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDto extends UserDto {

    private String username;
    private String email;
    private String phone;
    private Instant lastLogin;
    private Boolean blocked;
    private Boolean emailVerified;

}
