package com.kotyk.realtorconnect.dto.auth;

import com.kotyk.realtorconnect.dto.user.UserDto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private Token token;
    private UserDto user;

    @Getter
    @AllArgsConstructor
    public static class Token {
        private String authToken;
    }

}
