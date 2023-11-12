package com.kotyk.realtorconnect.dto.auth;

import com.kotyk.realtorconnect.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class JwtToken {

    private String username;
    private Role role;
    private Instant expiration;

}
