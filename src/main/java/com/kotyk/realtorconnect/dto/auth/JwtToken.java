package com.kotyk.realtorconnect.dto.auth;

import com.kotyk.realtorconnect.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtToken {

    private String username;
    private Role role;
    private Date expiration;

}
