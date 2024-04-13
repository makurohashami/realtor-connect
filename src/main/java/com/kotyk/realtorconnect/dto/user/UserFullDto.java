package com.kotyk.realtorconnect.dto.user;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
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
